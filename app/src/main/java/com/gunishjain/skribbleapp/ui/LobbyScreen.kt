package com.gunishjain.skribbleapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.gunishjain.skribbleapp.data.model.GamePhase

@Composable
fun LobbyScreen(
    navController: NavController
) {

//    Column(modifier = Modifier.fillMaxSize()) {
//        Text("Lobby: ${room?.name}", style = MaterialTheme.typography.h5)
//        Spacer(modifier = Modifier.height(16.dp))
//        Text("Players:")
//        room?.players?.forEach { player ->
//            Text("- ${player.name}")
//        }
//        Spacer(modifier = Modifier.height(16.dp))
//        Button(onClick = {
//            mainViewModel.updateGameState { it.copy(currentPhase = GamePhase.WORD_SELECTION) }
//            navController.navigate("game")
//        }) {
//            Text("Start Game")
//        }
//    }




}