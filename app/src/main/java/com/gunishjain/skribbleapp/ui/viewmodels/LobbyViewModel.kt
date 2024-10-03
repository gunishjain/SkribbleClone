package com.gunishjain.skribbleapp.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.gunishjain.skribbleapp.GameManager
import com.gunishjain.skribbleapp.data.model.Room
import com.gunishjain.skribbleapp.data.model.toJson
import com.gunishjain.skribbleapp.socket.SocketManager
import com.gunishjain.skribbleapp.ui.base.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

const val TAG="LobbyVM"

@HiltViewModel
class LobbyViewModel @Inject constructor(private val socketManager: SocketManager) : ViewModel() {

    private val _roomState = MutableStateFlow<UiState<Room?>>(UiState.Loading)
    val roomState: StateFlow<UiState<Room?>> = _roomState

    private val _isPartyLeader = MutableStateFlow<Boolean>(false)
    val isPartyLeader: StateFlow<Boolean> = _isPartyLeader

    init {
        listenForPlayerUpdates()
    }

    private fun listenForPlayerUpdates() {
        socketManager.on("playerJoined") { args ->
            if (args.isNotEmpty()) {
                val roomName = args[0] as String
                Log.d(TAG,"Player Joined in: $roomName")
                fetchRoomDetails(roomName)
            }
        }

        socketManager.on("playerDisconnected") { args ->
            if (args.isNotEmpty()) {
                val roomName = args[0] as String
                Log.d(TAG,"disconnect $roomName")
                fetchRoomDetails(roomName)
            }
        }

        socketManager.on("roomDetails") { args ->

            if (args.isNotEmpty()) {
                val roomInfo = args[0] as JSONObject
                Log.d(TAG,"Room Details: $roomInfo")
                handleRoomUpdate(roomInfo)
            }
        }

        socketManager.on("gameStarted") { args ->
            if (args.isNotEmpty()) {
                val gameState = args[0] as JSONObject
                Log.d("Game Started Data",gameState.toString())
                GameManager.startGame()
            }
        }

        socketManager.on("wordSelection") { args ->
            if (args.isNotEmpty()) {
                val words = args[0] as String
                // Handle word selection phase
            }
        }


    }


    fun fetchRoomDetails(roomName: String) {
        _roomState.value = UiState.Loading

        val roomData = JSONObject().apply {
            put("roomName", roomName)
        }

        // Emit request for the room details
        socketManager.emit("getRoomDetails", roomData)

        // Handle potential errors like room not found
        socketManager.on("roomError") { args ->
            if (args.isNotEmpty()) {
                val errorMessage = args[0] as String
                handleRoomError(errorMessage)
            }
        }
    }


    private fun handleRoomUpdate(roomInfo: JSONObject) {
        viewModelScope.launch {
        val gson = Gson()
        val updatedRoom: Room = gson.fromJson(roomInfo.toString(), Room::class.java)

            _roomState.value = UiState.Success(updatedRoom)

            val currentUser = updatedRoom?.players?.find { it.id == socketManager.getSocketId() }

            _isPartyLeader.value = currentUser?.isPartyLeader ?: false

        Log.d(TAG, "_room StateFlow Value: ${_roomState.value}")
        GameManager.initializeLobby(updatedRoom)
        }
    }

    private fun handleRoomError(errorMessage: String) {
        viewModelScope.launch {
            _roomState.value = UiState.Error(errorMessage)
        }
    }

    fun startGame() {
        val roomState = _roomState.value
        if (roomState is UiState.Success && roomState.data != null) {
            socketManager.emit("startGame",roomState.data.name)
        }
    }




    fun disconnectSocket() {
        socketManager.disconnect()
    }

}