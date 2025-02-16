package com.divingseagull.emergencyseagull.composable

import java.io.File

interface AudioPlayer {
    fun playFile(file: File)
    fun stop()
}