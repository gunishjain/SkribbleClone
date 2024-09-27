package com.gunishjain.skribbleapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.gunishjain.skribbleapp.data.model.Room
import com.gunishjain.skribbleapp.socket.SocketManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class JoinRoomViewModel @Inject constructor(private val socketManager: SocketManager) : ViewModel() {

    private val _roomJoined = MutableSharedFlow<Room>()
    val roomJoined: SharedFlow<Room> = _roomJoined.asSharedFlow()

    private val _joinRoomError = MutableSharedFlow<String>()
    val joinRoomError: SharedFlow<String> = _joinRoomError.asSharedFlow()

    fun joinRoom(roomName: String, playerName: String) {
        val joinData = JSONObject().apply {
            put("roomName", roomName)
            put("playerName", playerName)
        }
        socketManager.emit("joinRoom", joinData)

        socketManager.on("roomJoined") { args ->
            if (args.isNotEmpty()) {
                val roomInfo = args[0] as JSONObject
                handleRoomJoined(roomInfo)
            }
        }

        socketManager.on("roomNotFound") { args ->
            if (args.isNotEmpty()) {
                val error = args[0] as String
                handleRoomNotFound(error)
            }
        }
    }

    private fun handleRoomJoined(roomInfo: JSONObject) {
        viewModelScope.launch {
            val gson = Gson()
            val joinedRoom: Room = gson.fromJson(roomInfo.toString(), Room::class.java)
            _roomJoined.emit(joinedRoom)
        }
    }

    private fun handleRoomNotFound(error: String) {
        viewModelScope.launch {
            _joinRoomError.emit(error)
        }
    }

    override fun onCleared() {
        super.onCleared()
//        socketManager.off("roomJoined")  // Remove socket listener when ViewModel is destroyed
//        socketManager.off("roomNotFound")
    }

}