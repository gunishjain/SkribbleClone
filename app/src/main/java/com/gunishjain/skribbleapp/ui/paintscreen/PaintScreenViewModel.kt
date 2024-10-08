package com.gunishjain.skribbleapp.ui.paintscreen

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gunishjain.skribbleapp.data.model.Room
import com.gunishjain.skribbleapp.data.model.toJson
import com.gunishjain.skribbleapp.socket.SocketManager
import com.gunishjain.skribbleapp.util.PaintData
import com.gunishjain.skribbleapp.util.Point
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaintScreenViewModel @Inject constructor(private val socketManager: SocketManager): ViewModel() {

        private val _uiState = MutableStateFlow("")
            val uiState : StateFlow<String> = _uiState
        private val _roomInfo = MutableStateFlow<Room>(Room("","",0,0))
            val roomInfo : StateFlow<Room> = _roomInfo

        private val _paintData = MutableStateFlow<PaintData>(
            PaintData(Point(0.0.toFloat(),0.0.toFloat()),Color.White.toArgb(),0.0.toFloat(),""))
            val paintData : StateFlow<PaintData> = _paintData

//    init {
//        connectToServer()
//    }

     fun connectToServer() {
         viewModelScope.launch {
             socketManager.connect()
         }
    }

    fun sendCreateRoomDetail(room: String){
        viewModelScope.launch {
            socketManager.sendRoomData(room)
        }
    }

    fun sendJoinRoomDetail(joinroom: String){
        viewModelScope.launch {
            socketManager.sendJoinRoomData(joinroom)
        }
    }

    fun sendPaintDetail(data: PaintData){
        viewModelScope.launch {
            data.toJson()?.let { socketManager.sendPaintData(it) }
        }
    }

    fun updateRoom(){
        viewModelScope.launch {
            socketManager.updatedRoomDetails {
                Log.d("PaintScreenViewModel", "Received room update: $it")
                _roomInfo.value = it
                Log.d("PaintScreenViewModel", "Updated _roomInfo: ${_roomInfo.value}")
            }
        }
    }

    fun getPaintData(){
        viewModelScope.launch {
            socketManager.receivePaintData {
                _paintData.value = it
                Log.d("paintdata", it.toString())
            }
        }
    }

    fun isConnected(): Boolean {
        return socketManager.isConnected()
    }

    fun listenForErrors() {
        viewModelScope.launch {
            socketManager.onEvent("notCorrectGame") { errorMessage ->
                _uiState.value = errorMessage
            }
        }
    }

}