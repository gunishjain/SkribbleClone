package com.gunishjain.skribbleapp.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.gunishjain.skribbleapp.data.model.Room
import com.gunishjain.skribbleapp.data.model.toJson
import com.gunishjain.skribbleapp.socket.SocketManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONObject
import javax.inject.Inject

const val TAG="LobbyVM"

@HiltViewModel
class LobbyViewModel @Inject constructor(private val socketManager: SocketManager) : ViewModel() {

    private val _room = MutableStateFlow<Room?>(null)
    val room: StateFlow<Room?> = _room.asStateFlow()

    fun createRoom(roomName: String, playerName: String, maxPlayers: Int, maxRounds: Int) {

        val roomData = JSONObject().apply {
            put("roomName", roomName)
            put("playerName", playerName)
            put("maxPlayers", maxPlayers)
            put("maxRounds", maxRounds)
        }
        socketManager.emit("createRoom", roomData)

        socketManager.on("roomCreated") { args ->
            // Handle room created event, you can parse args to get room info
            if (args.isNotEmpty()) {
                val roomInfo = args[0] as JSONObject
                Log.d(TAG,roomInfo.toJson().toString())
                handleRoom(roomInfo)
            }
        }
    }

    fun joinRoom(roomName: String, playerName: String) {

        val joinData = JSONObject().apply {
            put("roomName", roomName)
            put("playerName", playerName)
        }
        socketManager.emit("joinRoom", joinData)

        socketManager.on("roomNotFound") { args ->
            if (args.isNotEmpty()) {
                val invalidRoomName = args[0] as String
                Log.e("JoinRoomError", "Room not found: $invalidRoomName")
            }
        }

        // Listen for roomJoined event
        socketManager.on("roomJoined") { args ->
            if (args.isNotEmpty()) {
                val roomInfo = args[0] as JSONObject
                handleRoom(roomInfo)
            }
        }
    }

    private fun handleRoom(roomInfo: JSONObject) {
        val gson = Gson()
        val room: Room = gson.fromJson(roomInfo.toString(), Room::class.java)
        Log.d(TAG, "Room Details: $room")
    }

    fun disconnectSocket() {
        socketManager.disconnect()
    }


}