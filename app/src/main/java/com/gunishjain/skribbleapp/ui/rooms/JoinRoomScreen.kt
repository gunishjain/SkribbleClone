package com.gunishjain.skribbleapp.ui.rooms


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


@Preview
@Composable
fun JoinRoom() {

    var roomFieldState by remember {
        mutableStateOf("")
    }

    var userFieldState by remember {
        mutableStateOf("")
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
        Button(onClick = { /*TODO*/ }) {
            Text(text = "Join Room")
        }
    }
}

