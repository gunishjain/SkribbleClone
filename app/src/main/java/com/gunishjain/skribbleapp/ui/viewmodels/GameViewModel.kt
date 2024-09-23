package com.gunishjain.skribbleapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.gunishjain.skribbleapp.data.model.GameState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(): ViewModel() {

    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    fun updateGameState(update: (GameState) -> GameState) {
        _gameState.update(update)
    }

    // Add methods to handle different game events
    fun startNewRound() { /* ... */ }
    fun updateTimeRemaining(time: Int) { /* ... */ }
    fun submitGuess(playerId: String, guess: String) { /* ... */ }
    fun endRound() { /* ... */ }
    // ... other methods as needed

}