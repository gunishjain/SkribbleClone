package com.gunishjain.skribbleapp.socket

import com.gunishjain.skribbleapp.util.Constants.Companion.SERVER_URL
import io.socket.client.IO
import io.socket.client.Socket
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

    fun emit(event: String, data: Any) {
        socket?.emit(event, data)
    }

    fun on(event: String, callback: (Array<Any>) -> Unit) {
        socket?.on(event) { callback(it) }
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


}
