package com.gunishjain.skribbleapp

import com.gunishjain.skribbleapp.data.model.GamePhase
import com.gunishjain.skribbleapp.data.model.GameState
import com.gunishjain.skribbleapp.data.model.Room
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object GameManager {

    private val _gameState = MutableStateFlow(
        GameState(
            currentPhase = GamePhase.LOBBY,
            currentRound = 0,
            totalRounds = 0,
            currentWord = "",
            timeRemaining = 0,
            players = emptyList()
        )
    )
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    private fun updateGameState(newState: GameState) {
        _gameState.value = newState
    }


    fun initializeLobby(room: Room) {
        updateGameState(
            GameState(
                currentPhase = GamePhase.LOBBY,
                currentRound = 0,
                totalRounds = room.maxRounds,
                currentWord = "",
                timeRemaining = 0,
                players = room.players
            )
        )
    }


}