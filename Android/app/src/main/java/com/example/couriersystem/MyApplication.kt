package com.example.couriersystem

import android.app.Application
import android.content.Context

class MyApplication : Application() {
    companion object {
        private lateinit var instance: MyApplication

        fun getInstance(): MyApplication {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    fun getContext(): Context {
        return applicationContext
    }
}