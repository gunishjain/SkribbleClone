package com.gunishjain.skribbleapp.data.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

data class DrawingPoint(
    val x: Float,
    val y: Float,
    val color: Int = Color.Black.toArgb(),
    val strokeWidth: Float = 5f,
    val isNewLine: Boolean = false
)
