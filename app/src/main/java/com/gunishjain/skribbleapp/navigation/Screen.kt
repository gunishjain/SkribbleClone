package com.gunishjain.skribbleapp.navigation

sealed class Screen(val route: String) {
    object Home: Screen(route = "home_screen")
    object CreateRoom: Screen(route = "create_room")
    object JoinRoom: Screen(route = "join_room")
    object PaintScreen: Screen(route = "paint_screen")
}
