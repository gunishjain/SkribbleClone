package com.gunishjain.skribbleapp.ui.viewmodels

import android.util.Log
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
class CreateRoomViewModel @Inject constructor(private val socketManager: SocketManager): ViewModel() {

    private val _roomCreated = MutableSharedFlow<Room>()
    val roomCreated: SharedFlow<Room> = _roomCreated.asSharedFlow()

    fun createRoom(roomName: String, playerName: String, maxPlayers: Int, maxRounds: Int) {
        val roomData = JSONObject().apply {
            put("roomName", roomName)
            put("playerName", playerName)
            put("maxPlayers", maxPlayers)
            put("maxRounds", maxRounds)
        }
        socketManager.emit("createRoom", roomData)

        socketManager.on("roomCreated") { args ->
            if (args.isNotEmpty()) {
                val roomInfo = args[0] as JSONObject
                Log.d("CRVM",roomInfo.toString())
                handleRoomCreation(roomInfo)
            }
        }
    }

    private fun handleRoomCreation(roomInfo: JSONObject) {
        viewModelScope.launch {
            val gson = Gson()
            val createdRoom: Room = gson.fromJson(roomInfo.toString(), Room::class.java)
            _roomCreated.emit(createdRoom)
        }
    }

    override fun onCleared() {
        super.onCleared()
//        socketManager.off("roomCreated")  // Remove socket listener when ViewModel is destroyed
    }

}