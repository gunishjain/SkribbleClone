package com.gunishjain.skribbleapp.navigation


sealed class Screen(val route: String) {
    object Home: Screen(route = "main_menu")
    object CreateRoom: Screen(route = "create_room")
    object JoinRoom: Screen(route = "join_room")
    object Lobby : Screen(route="lobby")
//    object PaintScreen: Screen(route = "paint_screen?room={room}&join={join}")

}
