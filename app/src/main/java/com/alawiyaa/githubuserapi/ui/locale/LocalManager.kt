package com.alawiyaa.githubuserapi.ui.locale

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.preference.PreferenceManager
import androidx.annotation.StringDef
import java.util.*

object LocalManager {
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    @StringDef(
        INDONESIA,
        ENGLISH
    )
    annotation class LocaleDef {
        companion object {
            var SUPPORTED_LOCALES =
                arrayOf<String>(
                    INDONESIA,
                    ENGLISH
                )
        }
    }

    const val ENGLISH:String = "en"
    const val INDONESIA:String = "in"


    private const val LANGUAGE_KEY = "language_key"


    fun setLocale(mContext: Context): Context? {
        return updateResources(
            mContext,
            getLanguagePref(mContext)
        )
    }


    fun setNewLocale(mContext: Context, @LocaleDef language: String): Context? {
        setLanguagePref(mContext, language)
        return updateResources(
            mContext,
            language
        )
    }

    @Suppress("DEPRECATION")
    fun getLanguagePref(mContext: Context?): String? {
        val mPreferences =
            PreferenceManager.getDefaultSharedPreferences(mContext)
        return mPreferences.getString(
            LANGUAGE_KEY,
            INDONESIA
        )
    }

    @Suppress("DEPRECATION")
    private fun setLanguagePref(
        mContext: Context,
        localeKey: String
    ) {
        val mPreferences =
            PreferenceManager.getDefaultSharedPreferences(mContext)
        mPreferences.edit().putString(LANGUAGE_KEY, localeKey).apply()
    }


    private fun updateResources(
        context: Context,
        language: String?
    ): Context? {
        var context = context
        val locale = Locale(language)
        Locale.setDefault(locale)
        val res = context.resources
        val config =
            Configuration(res.configuration)
        if (Build.VERSION.SDK_INT >= 17) {
            config.setLocale(locale)
            context =  context.createConfigurationContext(config)
        }else{
            config.locale
            res.updateConfiguration(config, res.displayMetrics)
        }
        return context
    }



}