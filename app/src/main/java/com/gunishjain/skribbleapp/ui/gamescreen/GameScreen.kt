package com.gunishjain.skribbleapp.ui.gamescreen

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.gunishjain.skribbleapp.ui.viewmodels.GameViewModel

@Composable
fun GameScreen(
    viewModel: GameViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    roomName: String?
) {


    
}