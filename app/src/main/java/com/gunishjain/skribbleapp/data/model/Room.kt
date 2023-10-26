package com.gunishjain.skribbleapp.data.model

import android.net.Uri
import com.google.gson.Gson

data class Room(
    val roomName: String,
    val nickname: String,
    val maxRounds: Int,
    val roomSize: Int
){
}



