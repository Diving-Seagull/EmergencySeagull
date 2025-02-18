package com.divingseagull.emergencyseagull.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.io.File
import java.util.concurrent.TimeUnit

interface SendingApi {
    @Multipart
    @POST("api/whisper/transcribe")
    suspend fun transcribe(
        @Part file: MultipartBody.Part,
        @Part("data") data: RequestBody
    )

    @POST("api/report")
    suspend fun sendTextData(
        @Body data: RequestBody
    )
}

class WhisperRepository(private val api: SendingApi) {
    suspend fun transcribeAudio(file: File, latitude: Double, longitude: Double) {
        val requestFile = file.asRequestBody("audio/mpeg".toMediaTypeOrNull())
        val filePart = MultipartBody.Part.createFormData("file", file.name, requestFile)

        val jsonData = JSONObject().apply {
            put("latitude", latitude)
            put("longitude", longitude)
        }.toString().toRequestBody("application/json".toMediaTypeOrNull())

        api.transcribe(filePart, jsonData)
    }

    suspend fun sendText(content: String, latitude: Double, longitude: Double) {
        val jsonString = JSONObject().apply {
            put("content", content)
            put("latitude", latitude)
            put("longitude", longitude)
        }.toString()

        Log.d("jsonString", jsonString)

        val jsonData = jsonString.toRequestBody("application/json".toMediaTypeOrNull())

        api.sendTextData(jsonData)
    }
}

class VM : ViewModel() {
    private val _latitude = MutableStateFlow<Double?>(null)
    private val _longitude = MutableStateFlow<Double?>(null)
    private val _cameraLatitude = MutableStateFlow<Double?>(null)
    private val _cameraLongitude = MutableStateFlow<Double?>(null)
    private val _audioFile = MutableStateFlow<File?>(null)
    private val _text = MutableStateFlow<String?>(null)
    private val _classification = MutableStateFlow<Int>(0)


    val latitude: StateFlow<Double?> = _latitude
    val longitude: StateFlow<Double?> = _longitude
    val audioFile: StateFlow<File?> = _audioFile
    val classification: StateFlow<Int> = _classification

    private val repository: WhisperRepository = WhisperRepository(provideRetrofit())

    fun updateLocation(lat: Double, long: Double) {
        _latitude.value = lat
        _longitude.value = long
    }

    fun updateAudioFile(file: File?) {
        _audioFile.value = file
    }

    fun updateText(text: String?) {
        _text.value = text
    }

    fun updateClassification(classification: Int) {
        _classification.value = classification
    }

    fun updateCameraLocation(lat: Double, long: Double) {
        _cameraLatitude.value = lat
        _cameraLongitude.value = long
        Log.d("cameraUpdate", "updateCameraLocation: $lat, $long")
    }

    fun uploadAudioWithMapPos() {
        val file = _audioFile.value ?: return
        val lat = _cameraLatitude.value ?: return
        val long = _cameraLongitude.value ?: return

        viewModelScope.launch {
            repository.transcribeAudio(file, lat, long)
        }
    }

    fun uploadAudioWithMyPos() {
        val file = _audioFile.value ?: return
        val lat = _latitude.value ?: return
        val long = _longitude.value ?: return

        viewModelScope.launch {
            repository.transcribeAudio(file, lat, long)
        }
    }

    fun uploadTextDataWithMapPos() {
        val lat = _cameraLatitude.value ?: return
        val long = _cameraLongitude.value ?: return
        val content = _text.value ?: return

        viewModelScope.launch {
            repository.sendText(content, lat, long)
        }
    }

    fun uploadTextDataWithMyPos() {
        val lat = _latitude.value ?: return
        val long = _longitude.value ?: return
        val content = _text.value ?: return

        viewModelScope.launch {
            repository.sendText(content, lat, long)
        }
    }
}

fun provideRetrofit(): SendingApi {
    val client = OkHttpClient.Builder()
        .followRedirects(true)
        .followSslRedirects(true)
        .retryOnConnectionFailure(true)
        .cache(null)
        .connectTimeout(5, TimeUnit.SECONDS)
        .writeTimeout(5, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .build()

    return Retrofit.Builder()
        .baseUrl("http://54.180.145.37:8080/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(SendingApi::class.java)
}