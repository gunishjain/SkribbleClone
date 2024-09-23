package com.gunishjain.skribbleapp.ui.paintscreen

import android.util.Log
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.unit.dp
import com.gunishjain.skribbleapp.util.PaintData
import com.gunishjain.skribbleapp.util.PathState

@Composable
fun PaintViewer(
    viewModel: PaintScreenViewModel
) {
    Log.d("JoinCheck-PaintViewer","here")
    val path = remember { mutableStateOf(mutableListOf<PathState>()) }
    Paint(path,viewModel)
    LaunchedEffect(Unit) {
        viewModel.getPaintData()
    }

}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Paint(path: MutableState<MutableList<PathState>>,
              paintScreenVM: PaintScreenViewModel
) {
    val room =paintScreenVM.roomInfo.collectAsState()
    val roomName = room.value.roomName
    val paintData=paintScreenVM.paintData.collectAsState()
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        val drawColor = remember{ mutableStateOf(Color.Black) }
        val drawBrush = remember{ mutableStateOf(5f) }
        val usedColor = remember { mutableStateOf(mutableSetOf(
            Color.Black,
            Color.White, Color.Gray))
        }

        path.value.add(PathState(Path(), Color(paintData.value.color),paintData.value.stroke))

        DrawingViewer(
            drawColor,
            drawBrush,
            usedColor,
            path.value,
            paintData.value

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
fun DrawingViewer(
    drawColor: MutableState<Color>,
    drawBrush: MutableState<Float>,
    usedColor: MutableState<MutableSet<Color>>,
    path: MutableList<PathState>,
    paintData: PaintData
) {
    val currentPath = path.last().path
    val movePath = remember{ mutableStateOf<Offset?>(null) }

    val receivedPoint = paintData.pathCordinate
    val receivedColor = Color(paintData.color)
    val receivedStroke = paintData.stroke
    val receivedPath = path.last().path

    Canvas(modifier = Modifier
        .fillMaxSize()
        .padding(top = 100.dp)
        .pointerInteropFilter {
            when (it.action) {
                MotionEvent.ACTION_DOWN -> { //started gesture to draw
                    currentPath.moveTo(it.x, it.y) //starts new subpath
                    usedColor.value.add(drawColor.value)

                }
                MotionEvent.ACTION_MOVE -> { //change is happening
                    movePath.value = Offset(it.x, it.y)

                }
                else -> {
                    movePath.value = null
                }
            }
            true
        }
    ){
        for (pathState in path) {
            drawPath(
                path = pathState.path,
                color = pathState.color,
                style = Stroke(pathState.stroke)
            )
        }

        if (receivedPoint.x != 0.0f && receivedPoint.y != 0.0f) {
            receivedPath.lineTo(receivedPoint.x, receivedPoint.y)
            receivedPath.moveTo(receivedPoint.x, receivedPoint.y)

            drawPath(
                path = receivedPath,
                color = receivedColor,
                style = Stroke(receivedStroke)
            )
        }
    }
}