package com.gunishjain.skribbleapp.ui.paintscreen

import android.util.Log
import androidx.lifecycle.ViewModel
import com.gunishjain.skribbleapp.data.model.Room
import com.gunishjain.skribbleapp.data.model.fromJson
import com.gunishjain.skribbleapp.data.model.toJson
import com.gunishjain.skribbleapp.socket.SocketManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PaintScreenViewModel @Inject constructor(private val socketManager: SocketManager): ViewModel() {

        private val _uiState = MutableStateFlow("")
            val uiState : StateFlow<String> = _uiState
        private val _roomInfo = MutableStateFlow<Room>(Room("","",0,0))
            val roomInfo : StateFlow<Room> = _roomInfo

    init {
        connectToServer()
    }

     fun connectToServer() {
        socketManager.connect()
    }

    fun receiveMessage(){
        socketManager.onMessageReceived {
            _uiState.value=it
        }
    }

    fun sendMessage(text: String){
        socketManager.sendMessage(text)

    }

    fun sendRoomDetail(room: String){
        socketManager.sendRoomData(room)
    }

    fun sendJoinRoomDetail(joinroom: String){
        socketManager.sendJoinRoomData(joinroom)
    }

    fun updateRoom(){
        socketManager.updatedRoomDetails {
            _roomInfo.value=it
        }
    }

    fun isConnected(): Boolean {
        return socketManager.isConnected()
    }

}