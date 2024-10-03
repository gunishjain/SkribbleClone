package com.gunishjain.skribbleapp

import com.gunishjain.skribbleapp.data.model.GamePhase
import com.gunishjain.skribbleapp.data.model.GameState
import com.gunishjain.skribbleapp.data.model.Room
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object GameManager {

    private const val ROUND_TIME = 60
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

    fun startGame() {
        val currentState = _gameState.value
        if (currentState.players.size < 2) return

        val newState = currentState.copy(
            currentPhase = GamePhase.WORD_SELECTION,
            currentRound = 1,
            currentDrawer = currentState.players.first()
        )
        updateGameState(newState)
    }

    fun selectWord(word: String) {
        val currentState = _gameState.value
        if (currentState.currentPhase != GamePhase.WORD_SELECTION) return

        val newState = currentState.copy(
            currentPhase = GamePhase.DRAWING,
            currentWord = word,
            timeRemaining = ROUND_TIME
        )
        updateGameState(newState)
//        startTimer()
    }


}