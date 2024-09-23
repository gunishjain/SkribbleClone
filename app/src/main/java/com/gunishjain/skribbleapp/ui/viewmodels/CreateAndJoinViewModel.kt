package com.gunishjain.skribbleapp.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gunishjain.skribbleapp.data.model.Room
import com.gunishjain.skribbleapp.socket.SocketManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CreateAndJoinViewModel @Inject constructor(private val socketManager: SocketManager): ViewModel() {

    private val _uiState = MutableStateFlow("NULL")
    val uiState : StateFlow<String> = _uiState
    private val _roomInfo = MutableStateFlow<Room>(Room("","",0,0))
    val roomInfo : StateFlow<Room> = _roomInfo


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

    fun listenForErrors() {
        viewModelScope.launch {
            socketManager.onEvent("notCorrectGame") { errorMessage ->
                Log.d("CAJVM",errorMessage)
                _uiState.value = errorMessage
            }
        }
    }


}