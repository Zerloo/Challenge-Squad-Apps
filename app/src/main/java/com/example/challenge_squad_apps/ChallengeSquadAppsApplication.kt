package com.example.challenge_squad_apps

import android.app.Application
import android.content.Context

class ChallengeSquadAppsApplication:  Application() {

    init {
        instance = this
    }

    companion object {
        private lateinit var instance: ChallengeSquadAppsApplication

        fun applicationContext() : Context {
            return instance.applicationContext
        }
    }
}