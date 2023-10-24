package com.gunishjain.skribbleapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.gunishjain.skribbleapp.ui.homescreen.HomeScreen
import com.gunishjain.skribbleapp.ui.paintscreen.PaintLayout
import com.gunishjain.skribbleapp.ui.rooms.CreateRoom
import com.gunishjain.skribbleapp.ui.rooms.JoinRoom

@Composable
fun SetupNavGraph(
    navController: NavHostController
) {
    NavHost(navController = navController,
    startDestination = Screen.Home.route){

      composable(
          route = Screen.Home.route
      )  {
          HomeScreen(navController)
      }

        composable(
            route = Screen.CreateRoom.route
        )  {
            CreateRoom()
        }

        composable(
            route = Screen.JoinRoom.route
        )  {
            JoinRoom()
        }

        composable(
            route = Screen.PaintScreen.route
        )  {
            PaintLayout()
        }


    }
}