package com.gunishjain.skribbleapp.ui.paintscreen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.gunishjain.skribbleapp.data.model.toJson

@Composable
fun <T>PaintLayout(
    navController: NavController,
    room: T
) {
    val previousDestination: String? = navController.previousBackStackEntry?.destination?.route
    val cameFromCreateRoom = previousDestination == "create_room"
    val paintViewModel : PaintScreenViewModel = hiltViewModel()

    LaunchedEffect(Unit) {
        paintViewModel.connectToServer()
    }


    LaunchedEffect(room) {
        room.toJson()?.let { roomJson ->
            if (cameFromCreateRoom) {
                paintViewModel.sendCreateRoomDetail(roomJson)
            } else {
                paintViewModel.sendJoinRoomDetail(roomJson)
            }
            paintViewModel.updateRoom()
        }
    }

    val roomInfo by paintViewModel.roomInfo.collectAsState()

    if (cameFromCreateRoom) {
        Painter(paintViewModel)
    } else {
        PaintViewer(viewModel = paintViewModel)
    }


    LaunchedEffect(roomInfo) {
        Log.d("PaintLayout", "Current room info: $roomInfo")
    }

}


