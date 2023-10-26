package com.gunishjain.skribbleapp.data.model

import android.net.Uri
import com.google.gson.Gson

data class Room(
    val roomName: String,
    val roomLeader: String,
    val maxRounds: Int,
    val roomSize: Int
){
    override fun toString(): String = Uri.encode(Gson().toJson(this))
}



