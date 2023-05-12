package com.example.snacksprint

import android.app.Application

class SnackSprintApp: Application() {
    //lateinit var appDatabase : AppDatabase
    override fun onCreate() {
        super.onCreate()
        // appDatabase = AppDatabase.getDatabase(this)
    }
}