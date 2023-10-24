package com.gunishjain.skribbleapp.di

import com.gunishjain.skribbleapp.socket.SocketManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSocketManager(): SocketManager {
        return SocketManager()
    }



}