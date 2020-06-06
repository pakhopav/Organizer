package com.example.pdaorganizer

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class App : Application() {
    object const {
        val CHANNEL_1_ID = "channel1"

    }
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }
    fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel1 = NotificationChannel(const.CHANNEL_1_ID, "Channel 1", NotificationManager.IMPORTANCE_HIGH )
            channel1.description = "This is notification channel 1"
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel1)

        }

    }
}