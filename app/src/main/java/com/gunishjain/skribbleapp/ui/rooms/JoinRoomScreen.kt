package com.gunishjain.skribbleapp.ui.rooms


import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.gunishjain.skribbleapp.navigation.Screen
import com.gunishjain.skribbleapp.ui.viewmodels.JoinRoomViewModel


@Composable
fun JoinRoom(
    navController: NavController
) {

    val viewModel : JoinRoomViewModel = hiltViewModel()
    val roomJoined by viewModel.roomJoined.collectAsState(initial = null)

    var roomFieldState by remember {
        mutableStateOf("")
    }

    var userFieldState by remember {
        mutableStateOf("")
    }

    LaunchedEffect(roomJoined) {
        roomJoined?.let {
            Log.d("JoinRoom Screen: ",it.toString())
            navController.navigate(Screen.Lobby.passRoomName(it.name)) // Navigate to lobby when room is joined
        }
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

                viewModel.joinRoom(roomFieldState,userFieldState)

            } else {
                Log.d("JoinRoom Screen","Enter the all Details")
            }
        }) {
            Text(text = "Join Room")
        }
    }


}

