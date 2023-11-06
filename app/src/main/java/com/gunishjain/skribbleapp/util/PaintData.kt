package com.gunishjain.skribbleapp.util


data class Point(val x:Float,val y:Float)

data class PaintData(
    val pathCordinate: Point,
    val color: Int,
    val stroke: Float,
    val roomName: String
)
