package com.gunishjain.skribbleapp.socket

import android.util.Log
import com.gunishjain.skribbleapp.data.model.Room
import com.gunishjain.skribbleapp.data.model.fromJson
import com.gunishjain.skribbleapp.util.Constants.Companion.SERVER_URL
import com.gunishjain.skribbleapp.util.PaintData
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONException
import org.json.JSONObject
import java.net.URISyntaxException

class SocketManager {
    private var socket: Socket? = null

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
            args.let { msg ->
                if (msg.isNotEmpty()) {
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
        Log.d("Create-Socket",room)
    }

    fun sendJoinRoomData(joinroom: String) {
        Log.d("Join-Socket",joinroom)
        socket?.emit("join-game", joinroom)
    }

    fun updatedRoomDetails(listener: (Room) -> Unit) {
        socket?.on("updateRoom") { args ->
            Log.d("SocketManager", "Received updateRoom event. Args: ${args.contentToString()}")

            args.let { msg ->
                if (msg.isNotEmpty()) {
                    try {
                        val jsonObject = msg[0] as JSONObject
                        val jsonString = jsonObject.toString()
                        Log.d("SocketManager", "Parsed JSON: $jsonString")
                        val room = Room(
                            roomName = jsonObject.optString("name"),
                            nickname = jsonObject.optJSONArray("players").optJSONObject(0)?.optString("nickname")!!,
                            maxRounds = jsonObject.optInt("maxRounds"),
                            roomSize = jsonObject.optInt("occupancy")
                        )
                        Log.d("SocketManager", "Parsed Room object: $room")
                        listener.invoke(room)
                    } catch (e: JSONException) {
                        Log.e("SocketManager", "Error parsing JSON", e)
                    } catch (e: Exception){
                        Log.e("SocketManager", "Error", e)
                    }
                } else {
                    Log.w("SocketManager", "Received empty args for updateRoom event")
                }
            }
        }
    }

    fun sendPaintData(data: String) {
        socket?.emit("paint", data)
        Log.d("Paint-Socket", data)
    }

    fun receivePaintData(listener: (PaintData) -> Unit) {
        socket?.on("paintData") { args ->
            args.let { msg ->
                if (msg.isNotEmpty()) {
                    try {
                        val jsonObject = msg[0] as JSONObject
                        val jsonString = jsonObject.toString()
                        Log.d("receiver-paint-socket",jsonString)
                        val pathState = jsonString.fromJson(PaintData::class.java)
                        listener.invoke(pathState)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    fun onEvent(event: String, listener: (String) -> Unit) {
        socket?.on(event) { args ->
            args.let { msg ->
                if (msg.isNotEmpty()) {
                    val message = args[0] as String
                    listener.invoke(message)
                }
            }
        }
    }


}
