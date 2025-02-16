package com.divingseagull.emergencyseagull.composable

import java.io.File

interface AudioRecorder {
    fun start(outputFile : File)
    fun stop()
}