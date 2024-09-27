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

    private val _currentPlayerId = MutableStateFlow<String?>(null)
    val currentPlayerId: StateFlow<String?> = _currentPlayerId.asStateFlow()

    init {
        listenForPlayerUpdates()
    }

    private fun listenForPlayerUpdates() {
        socketManager.on("playerJoined") { args ->
            if (args.isNotEmpty()) {
                val roomName = args[0] as String
                Log.d(TAG,"init $roomName")
                fetchRoomDetails(roomName)
            }
        }

        // Listen for updates to the room's state, e.g., new players joining
        socketManager.on("roomDetails") { args ->
            Log.d(TAG,"second function inside listen $args")
            if (args.isNotEmpty()) {
                val roomInfo = args[0] as JSONObject
                handleRoomUpdate(roomInfo)
            }
        }
    }


    fun fetchRoomDetails(roomName: String) {
        _roomState.value = UiState.Loading  // Indicate loading state when fetching room details

        val roomData = JSONObject().apply {
            put("roomName", roomName)
        }

        // Emit request for the room details
        socketManager.emit("getRoomDetails", roomData)

        // Listen for the server's response to 'getRoomDetails'
//        socketManager.on("roomDetails") { args ->
//            if (args.isNotEmpty()) {
//                val roomInfo = args[0] as JSONObject
//                handleRoomUpdate(roomInfo)
//            }
//        }

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
        Log.d(TAG, "Room Details: $updatedRoom")
        _roomState.value = UiState.Success(updatedRoom)
        Log.d(TAG, "_room StateFlow Value: ${_roomState.value}")
        GameManager.initializeLobby(updatedRoom)
        }
    }

    private fun handleRoomError(errorMessage: String) {
        viewModelScope.launch {
            _roomState.value = UiState.Error(errorMessage)  // Set the error state with the error message
        }
    }

    fun disconnectSocket() {
        socketManager.disconnect()
    }

}