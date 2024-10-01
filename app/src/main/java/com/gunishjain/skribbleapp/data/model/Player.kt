package com.gunishjain.skribbleapp.data.model

data class Player(
    val id: String,
    val name: String,
    val score: Int = 0,
    val isPartyLeader: Boolean
)
