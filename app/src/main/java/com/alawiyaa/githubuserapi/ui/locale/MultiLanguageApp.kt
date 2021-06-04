package com.alawiyaa.githubuserapi.ui.locale

import android.app.Application
import android.content.Context
import android.content.res.Configuration


class MultiLanguageApp:Application() {
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocalManager.setLocale(base))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LocalManager.setLocale(this)
    }
}