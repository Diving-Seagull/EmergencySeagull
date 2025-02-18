package com.divingseagull.emergencyseagulladmin.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ReportService {
    @GET("api/report/category/{category}")
    suspend fun getReports(
        @Path("category") category: String,
        @Query("inCharge") inCharge: String
    ): ReportResponse
}

interface DeleteService {
    @DELETE("api/report/{id}")
    suspend fun deleteReport(@Path("id") id: Long)
}

interface AcceptedService {
    @GET("api/report/accepted/{category}")
    suspend fun acceptReport(@Path("category") category: String): ReportResponse
}

data class ReportResponse(
    val totalPages: Int,
    val totalElements: Int,
    val content: List<Report>
)

data class Report(
    val id: Long,
    val content: String,
    val category: String,
    val sub_category: String,
    val latitude: Double,
    val longitude: Double,
    val created_at: String,
    val duplicate_count: Int,
    val address: String,
    val in_charge: String
)

class VM : ViewModel() {
    private val _district = MutableStateFlow<String?>(null)
    private val _classification = MutableStateFlow<String?>("동래구")
    private val _reports = MutableStateFlow<List<Report>>(emptyList())
    private val _acceptedReports = MutableStateFlow<List<Report>>(emptyList())
    private val _accepted = MutableStateFlow<String?>("화재")

    val district: StateFlow<String?> = _district
    val classification: StateFlow<String?> = _classification
    val reports: StateFlow<List<Report>> = _reports
    val accepted: StateFlow<String?> = _accepted
    val acceptedReports: StateFlow<List<Report>> = _acceptedReports

    private val repository: ReportService = provideRetrofit().create(ReportService::class.java)
    private val acceptedRepository: AcceptedService =
        provideRetrofit().create(AcceptedService::class.java)

    fun updateDistrict(newDistrict: String) {
        _district.value = newDistrict
    }

    fun updateClassification(newClassification: String) {
        _classification.value = newClassification
    }

    fun updateAccepted(newAccepted: String) {
        _accepted.value = newAccepted
    }

    fun fetchReports(onSuccess: (List<Report>) -> Unit, onError: (Throwable) -> Unit = {}) {
        val inCharge = _district.value ?: return
        val category = _classification.value ?: return

        viewModelScope.launch {
            try {
                val response = repository.getReports(category, inCharge)
                _reports.value = response.content
                onSuccess(response.content)
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    fun deleteReport(id: Long, onSuccess: () -> Unit, onError: (Throwable) -> Unit = {}) {
        val deleteService = provideRetrofit().create(DeleteService::class.java)
        viewModelScope.launch {
            try {
                deleteService.deleteReport(id)
                onSuccess()
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    fun acceptReport(onSuccess: (List<Report>) -> Unit, onError: (Throwable) -> Unit = {}) {
        val category = when (_accepted.value) {
            "화재" -> "FIRE"
            "구조" -> "RESCUE"
            "구급" -> "MEDICAL"
            "생활안전" -> "SAFETY"
            else -> return
        }
        viewModelScope.launch {
            try {
                val response = acceptedRepository.acceptReport(category)
                _acceptedReports.value = response.content
                onSuccess(response.content)
            } catch (e: Exception) {
                onError(e)
            }
        }
    }
}

fun provideRetrofit(): Retrofit {
    return Retrofit.Builder()
        .baseUrl("http://54.180.145.37:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}