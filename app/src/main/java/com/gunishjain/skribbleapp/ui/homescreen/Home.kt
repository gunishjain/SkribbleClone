package com.gunishjain.skribbleapp.ui.homescreen

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gunishjain.skribbleapp.navigation.Screen



@Composable
fun HomeScreen(
    navController: NavController
) {

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Text(text = "Create Room or Join to Play",
            color = Color.Black
        )
        
        Spacer(modifier = Modifier.height(5.dp))

        Row(modifier = Modifier.padding(5.dp),
            verticalAlignment = Alignment.CenterVertically) {

            Button(onClick = {
                navController.navigate(
                    route = Screen.CreateRoom.route)
            }) {
                Text(text = "Create Room")
            }
            Spacer(modifier = Modifier.width(13.dp))

            Button(onClick = {
                navController.navigate(
                    route=Screen.JoinRoom.route)
            }) {
                Text(text = "Join Room")
            }

        }
        
    }


}