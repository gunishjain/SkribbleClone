package com.gunishjain.skribbleapp

import android.app.Application
import com.gunishjain.skribbleapp.socket.SocketManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class BaseClass : Application() {

    @Inject
    lateinit var socketManager: SocketManager

    override fun onCreate() {
        super.onCreate()

        socketManager.connect()
    }

    override fun onTerminate() {
        super.onTerminate()

        // Disconnect the socket when the app is terminated
        socketManager.disconnect()
    }
}