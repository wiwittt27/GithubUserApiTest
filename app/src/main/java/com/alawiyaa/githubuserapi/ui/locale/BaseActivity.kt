package com.alawiyaa.githubuserapi.ui.locale

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resetTitle()
    }
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocalManager.setLocale(base))
    }



    private fun resetTitle() {
        try {
            val info = packageManager.getActivityInfo(
                componentName,
                PackageManager.GET_META_DATA
            )
            if (info.labelRes != 0) {
                setTitle(info.labelRes)
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

    }
}