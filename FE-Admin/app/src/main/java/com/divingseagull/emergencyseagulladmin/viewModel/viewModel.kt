package com.divingseagull.emergencyseagulladmin.viewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class VM : ViewModel() {
    private val _district = MutableStateFlow<String?>(null)
    private val _classification = MutableStateFlow<String?>("동래구")

    val district: StateFlow<String?> = _district
    val classification: StateFlow<String?> = _classification

    fun updateDistrict(newDistrict: String) {
        _district.value = newDistrict
    }

    fun updateClassification(newClassification: String) {
        _classification.value = newClassification
    }



}