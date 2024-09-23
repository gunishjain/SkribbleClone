package com.gunishjain.skribbleapp.data.model

import com.google.gson.annotations.SerializedName


data class Room(
    @SerializedName("_id") val id: String,
    val name: String,
    val players: List<Player> = emptyList(),
    val maxPlayers: Int,
    val maxRounds: Int
)


