package com.gunishjain.skribbleapp.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.gunishjain.skribbleapp.data.model.GamePhase
import com.gunishjain.skribbleapp.data.model.Player
import com.gunishjain.skribbleapp.data.model.toJson
import com.gunishjain.skribbleapp.ui.base.UiState
import com.gunishjain.skribbleapp.ui.viewmodels.LobbyViewModel

@Composable
fun LobbyScreen(
    navController: NavController,
    roomName: String?
) {
    val viewModel : LobbyViewModel = hiltViewModel()
    val roomState = viewModel.roomState.collectAsStateWithLifecycle()
    val roomInfo = roomState.value
    val isPartyLeader = viewModel.isPartyLeader.collectAsStateWithLifecycle().value


    LaunchedEffect(roomName) {
        viewModel.fetchRoomDetails(roomName!!)
        Log.d("LobbyScreen", "Room state collected: ${roomInfo.toJson()}")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {


        when (roomInfo) {
            is UiState.Success -> {
                Text(
                    text = "Lobby: ${roomInfo.data?.name ?: ""}",
                    style = MaterialTheme.typography.h3,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = "Connected Players:",
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    items(roomInfo.data?.players ?: emptyList()) { player ->
                        Log.d("screen", player.name)
                        PlayerItem(player, player.id == roomInfo.data?.players?.firstOrNull()?.id)
                    }
                }

                if (isPartyLeader) {
                    Button(onClick = { /* Start game action */ }) {
                        Text("Start Game")
                    }
                }

            }

            is UiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(60.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }

            is UiState.Error -> {
                Log.d("LobbyScreen", roomInfo.message)
            }
        }
    }

}

@Composable
fun PlayerItem(player: Player, isRoomCreator: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = player.name,
            style = MaterialTheme.typography.body1,
            modifier = Modifier.weight(1f)
        )
        if (isRoomCreator) {
            Text(
                text = "(Creator)",
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.secondary
            )
        }
    }
}

@Composable
fun ShowToastMessage(message: String) {
    val context = LocalContext.current

    // Call this function wherever you want to show the toast
    LaunchedEffect(message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}


