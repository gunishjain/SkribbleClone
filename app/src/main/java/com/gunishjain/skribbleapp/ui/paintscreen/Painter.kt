package com.gunishjain.skribbleapp.ui.paintscreen

import android.util.Log
import androidx.compose.ui.graphics.Color
import android.view.MotionEvent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.unit.dp
import com.gunishjain.skribbleapp.util.PaintData
import com.gunishjain.skribbleapp.util.PathState
import com.gunishjain.skribbleapp.util.Point

@Composable
fun Painter(
    viewModel: PaintScreenViewModel
) {
    val path = remember {mutableStateOf(mutableListOf<PathState>())}
    PaintBody(path,viewModel)
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PaintBody(path: MutableState<MutableList<PathState>>,
paintScreenVM: PaintScreenViewModel
) {

    val room =paintScreenVM.roomInfo.collectAsState()
    val roomName = room.value.roomName
    Log.d("Painter-composable",room.toString())

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        val drawColor = remember{ mutableStateOf(Color.Black)}
        val drawBrush = remember{ mutableStateOf(5f)}
        val usedColor = remember { mutableStateOf(mutableSetOf(Color.Black,Color.White, Color.Gray))}

        path.value.add(PathState(Path(),drawColor.value,drawBrush.value))

        DrawingCanvas(
            drawColor,
            drawBrush,
            usedColor,
            path.value,
            sendDrawingDataToBackend = {
                paintScreenVM.sendPaintDetail(PaintData(it,drawColor.value.toArgb(),drawBrush.value,roomName!!))
            }
        )

        DrawingTools(
            drawColor = drawColor,
            drawBrush = drawBrush,
            usedColors = usedColor.value
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DrawingCanvas(
    drawColor: MutableState<Color>,
    drawBrush: MutableState<Float>,
    usedColor: MutableState<MutableSet<Color>>,
    path: MutableList<PathState>,
    sendDrawingDataToBackend: (Point) -> Unit
) {
    val currentPath = path.last().path
    val movePath = remember{ mutableStateOf<Offset?>(null)}

    Canvas(modifier = Modifier
        .fillMaxSize()
        .padding(top = 100.dp)
        .pointerInteropFilter {
            when (it.action) {
                MotionEvent.ACTION_DOWN -> {
                    //started gesture to draw
                    currentPath.moveTo(it.x, it.y) //starts new subpath
                    usedColor.value.add(drawColor.value)

                    sendDrawingDataToBackend(Point(it.x,it.y))
                }
                MotionEvent.ACTION_MOVE -> { //change is happening
                    movePath.value = Offset(it.x, it.y)
                    sendDrawingDataToBackend(Point(it.x,it.y))

                }
                else -> {
                    movePath.value = null
                }
            }
            true
        }
    ){
        movePath.value?.let {
            currentPath.lineTo(it.x,it.y) //adds lines from movepath to the current path
            drawPath(
                path = currentPath,
                color = drawColor.value,
                style = Stroke(drawBrush.value)
            )
        }
        path.forEach {
            drawPath(
                path = it.path,
                color = it.color,
                style  = Stroke(it.stroke)
            )
        }
    }
}


