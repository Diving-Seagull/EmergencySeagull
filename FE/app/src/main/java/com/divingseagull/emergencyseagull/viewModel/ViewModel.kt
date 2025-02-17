package com.divingseagull.emergencyseagull.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File

class VM : ViewModel() {
    private val _latitude = MutableStateFlow<Double?>(null)
    private val _longitude = MutableStateFlow<Double?>(null)
    private val _cameraLatitude = MutableStateFlow<Double?>(null)
    private val _cameraLongitude = MutableStateFlow<Double?>(null)

    private val _audioFile = MutableStateFlow<File?>(null)


    val latitude: StateFlow<Double?> = _latitude
    val longitude: StateFlow<Double?> = _longitude
    val cameraLatitude: StateFlow<Double?> = _cameraLatitude
    val cameraLongitude: StateFlow<Double?> = _cameraLongitude
    val audioFile: StateFlow<File?> = _audioFile

    fun updateLocation(lat: Double, long: Double) {
        _latitude.value = lat
        _longitude.value = long
    }

    fun updateAudioFile(file: File?) {
        _audioFile.value = file
    }

    fun updateCameraLocation(lat: Double, long: Double) {
        _cameraLatitude.value = lat
        _cameraLongitude.value = long
        Log.d("cameraUpdate", "updateCameraLocation: $lat, $long")
    }
}