package com.gunishjain.skribbleapp.ui.paintscreen

import android.util.Log
import androidx.lifecycle.ViewModel
import com.gunishjain.skribbleapp.socket.SocketManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


@HiltViewModel
class PaintScreenViewModel @Inject constructor(private val socketManager: SocketManager): ViewModel() {

private val _uiState = MutableStateFlow("")
    val uiState : StateFlow<String> = _uiState
    init {
        connectToServer()
    }

     fun connectToServer() {
        socketManager.connect()
    }

    fun receiveMessage(){
        socketManager.onMessageReceived {
            Log.d("Gunish-vm",it)
            _uiState.value=it
        }
    }

    fun sendMessage(text: String){
        socketManager.sendMessage(text)

    }

    fun isConnected(): Boolean {
        return socketManager.isConnected()
    }


}