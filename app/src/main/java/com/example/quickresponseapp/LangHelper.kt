package com.example.quickresponseapp

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.preference.PreferenceManager
import java.util.*

object LangHelper {

    fun applySavedLocale(base: Context): Context {
        val prefs = PreferenceManager.getDefaultSharedPreferences(base)
        val langCode = prefs.getString("app_lang", "en") ?: "en"
        return updateLocale(base, langCode)
    }

    fun updateLocale(context: Context, langCode: String): Context {
        val locale = Locale(langCode)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)

        // Update resources to reflect the new locale
        context.resources.updateConfiguration(config, context.resources.displayMetrics)

        return context.createConfigurationContext(config)
    }
}
