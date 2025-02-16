package com.divingseagull.emergencyseagull.viewModel

import androidx.lifecycle.ViewModel

class VM : ViewModel() {
    //dummy
    var count = 0

    fun increment() {
        count++
    }
}