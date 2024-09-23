package com.gunishjain.skribbleapp.data.model


enum class GamePhase {
    LOBBY, WORD_SELECTION, DRAWING, ROUND_END, GAME_END
}

data class GameState(
    val currentPhase: GamePhase = GamePhase.LOBBY,
    val currentRound: Int,
    val totalRounds: Int,
    val currentDrawer: Player? = null,
    val currentWord: String,
    val timeRemaining: Int,
    val players: List<Player> = emptyList()
)
