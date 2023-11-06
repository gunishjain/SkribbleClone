package com.gunishjain.skribbleapp.ui.paintscreen

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


    if(cameFromCreateRoom){
        room.toJson()?.let { paintViewModel.sendRoomDetail(it)
            LaunchedEffect(Unit){
                paintViewModel.updateRoom()
            }
            Painter(paintViewModel)
        }
    } else {
        room.toJson()?.let {
            LaunchedEffect(Unit){
                paintViewModel.sendJoinRoomDetail(it)
                paintViewModel.updateRoom()
            }
            PaintViewer(viewModel =paintViewModel )
        }
    }
    

}
@Composable
fun PaintScreen(
    paintVM: PaintScreenViewModel
) {
    var userFieldState by remember {
        mutableStateOf("")
    }
    val uiState by paintVM.uiState.collectAsState()

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        TextField(
            value = userFieldState,
            label = { Text(text = "Enter Your Name") },
            onValueChange = {
                userFieldState=it },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = {
            paintVM.sendMessage(userFieldState)
            paintVM.receiveMessage()
        }) {
            Text(text = "Click Me")
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = uiState)
    }

}

