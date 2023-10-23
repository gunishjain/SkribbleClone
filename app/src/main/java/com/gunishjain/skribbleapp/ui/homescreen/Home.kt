package com.gunishjain.skribbleapp.ui.homescreen

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Preview
@Composable
fun HomeScreen() {

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Text(text = "Create Room or Join to Play",
            color = Color.Black
        )
        
        Spacer(modifier = Modifier.height(5.dp))

        Row(modifier = Modifier.padding(5.dp),
            verticalAlignment = Alignment.CenterVertically) {

            Button(onClick = { /*TODO*/ }) {
                Text(text = "Create Room")
            }
            Spacer(modifier = Modifier.width(13.dp))

            Button(onClick = { /*TODO*/ }) {
                Text(text = "Join Room")
            }

        }
        
    }


}