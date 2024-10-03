package com.gunishjain.skribbleapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.gunishjain.skribbleapp.ui.LobbyScreen
import com.gunishjain.skribbleapp.ui.gamescreen.GameScreen
import com.gunishjain.skribbleapp.ui.homescreen.MainMenuScreen
import com.gunishjain.skribbleapp.ui.rooms.CreateRoomScreen
import com.gunishjain.skribbleapp.ui.rooms.JoinRoom

@Composable
fun SetupNavGraph(
    navController: NavHostController
) {
    NavHost(navController = navController,
    startDestination = Screen.Home.route) {

        composable(
            route = Screen.Home.route
        ) {
            MainMenuScreen(navController)
        }

        composable(
            route = Screen.CreateRoom.route
        ) {
            CreateRoomScreen(navController)
        }

        composable(
            route = Screen.JoinRoom.route
        ) {
            JoinRoom(navController)
        }

        composable(
            route = Screen.Lobby.route,
            arguments = listOf(navArgument("roomName") { type = NavType.StringType })
        ) { backStackEntry ->
            val roomName = backStackEntry.arguments?.getString("roomName")
            LobbyScreen(navController, roomName)
        }

        composable(
            route = Screen.Game.route,
            arguments = listOf(navArgument("roomName") { type = NavType.StringType })
        ) { backStackEntry ->
            val roomName = backStackEntry.arguments?.getString("roomName")
            GameScreen(
                onNavigateBack = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }, roomName = roomName
            )


        }
    }
}