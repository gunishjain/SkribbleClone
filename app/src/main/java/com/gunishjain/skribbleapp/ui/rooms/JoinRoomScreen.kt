package com.gunishjain.skribbleapp.ui.rooms


import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.gunishjain.skribbleapp.data.model.JoinRoom
import com.gunishjain.skribbleapp.data.model.Room
import com.gunishjain.skribbleapp.data.model.toJson
import com.gunishjain.skribbleapp.ui.viewmodels.CreateAndJoinViewModel


@Composable
fun JoinRoom(
    navController: NavController
) {

    val createAndJoinViewModel : CreateAndJoinViewModel = hiltViewModel()
    val roomStatus by createAndJoinViewModel.uiState.collectAsState()

    var roomFieldState by remember {
        mutableStateOf("")
    }

    var userFieldState by remember {
        mutableStateOf("")
    }

    LaunchedEffect(Unit) {
        createAndJoinViewModel.connectToServer()
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Text(text = "Join Room",
            fontSize = 22.sp)

        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = roomFieldState,
            label = { Text(text = "Enter Room Name") },
            onValueChange = { roomFieldState=it },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = userFieldState,
            label = { Text(text = "Enter Your Name") },
            onValueChange = { userFieldState=it },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = {
            if(userFieldState.isNotEmpty() and roomFieldState.isNotEmpty()){
                val joinroom = JoinRoom(roomFieldState, userFieldState)
                val model = joinroom.toJson()
                createAndJoinViewModel.sendJoinRoomDetail(model!!)
                createAndJoinViewModel.listenForErrors()

            } else {
                Log.d("JoinRoom Screen","Enter the all Details")
            }
        }) {
            Text(text = "Join Room")
        }
    }

    LaunchedEffect(roomStatus) {
        if (roomStatus != "NULL") {
            if (roomStatus.contains("Please enter a valid room name")) {
                Log.d("JoinRoom Screen", "Error Creating ROOM: $roomStatus")
            } else {
                Log.d("JoinRoom Screen", "navigate: $roomStatus")

                //TODO: handle navigation
            }
        }
    }
}

