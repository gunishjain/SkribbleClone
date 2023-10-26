package com.gunishjain.skribbleapp.socket

import android.util.Log
import com.google.gson.JsonObject
import com.gunishjain.skribbleapp.data.model.Room
import com.gunishjain.skribbleapp.data.model.fromJson
import com.gunishjain.skribbleapp.data.model.toJson
import com.gunishjain.skribbleapp.util.Constants.Companion.SERVER_URL
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONException
import org.json.JSONObject
import java.net.URISyntaxException

class SocketManager {
    private var socket: Socket?=null

    init {
        try {
            socket = IO.socket(SERVER_URL)
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }

    fun connect() {
        socket?.connect()
    }

    fun disconnect() {
        socket?.disconnect()
    }

    fun isConnected(): Boolean {
        return socket?.connected() ?: false
    }

    fun onMessageReceived(listener: (String) -> Unit) {
        socket?.on("broadcast") { args ->
            args.let {msg->
                if(msg.isNotEmpty()){
                    val message = args[0] as String
                    listener.invoke(message)
                }
            }
        }
    }

    fun sendMessage(message: String) {
        socket?.emit("message", message)
    }

    fun sendRoomData(room: String) {
        socket?.emit("create-game", room)
    }

    fun updatedRoomDetails(listener: (Room) -> Unit){
        socket?.on("updateRoom") { args ->
            args.let {msg->
                if(msg.isNotEmpty()){
                    try {
                        val jsonObject = msg[0] as JSONObject
                        val jsonString = jsonObject.toString()
                        val room = jsonString.fromJson(Room::class.java)
                        listener.invoke(room)
                    } catch (e: JSONException) {
                        // Handle JSON parsing error
                        e.printStackTrace()
                    }
                }
            }
        }
    }
}
