package com.example.quickresponseapp

import android.content.Context
import android.content.res.Configuration
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.util.*

val Context.dataStore by preferencesDataStore("app_prefs")

object AppPreferences {

    private val LANG_KEY = stringPreferencesKey("app_lang")
    private val DISCLAIMER_SEEN_KEY = booleanPreferencesKey("disclaimer_seen")

    // Language Preference
    fun getLanguageFlow(context: Context): Flow<String> {
        return context.dataStore.data.map { prefs ->
            prefs[LANG_KEY] ?: "en"
        }
    }

    suspend fun getLanguage(context: Context): String {
        return context.dataStore.data.first()[LANG_KEY] ?: "en"
    }

    suspend fun setLanguage(context: Context, lang: String) {
        context.dataStore.edit { prefs ->
            prefs[LANG_KEY] = lang
        }
    }

    fun applySavedLocale(context: Context): Context {
        val langCode = runBlocking {
            context.dataStore.data.first()[LANG_KEY] ?: "en"
        }
        return updateLocale(context, langCode)
    }

    fun updateLocale(context: Context, langCode: String): Context {
        val locale = Locale(langCode)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)

        // Return a new context with the updated locale
        return context.createConfigurationContext(config)
    }

    // Disclaimer Flag
    fun hasSeenDisclaimer(context: Context): Flow<Boolean> {
        return context.dataStore.data.map { prefs ->
            prefs[DISCLAIMER_SEEN_KEY] ?: false
        }
    }

    suspend fun setDisclaimerSeen(context: Context) {
        context.dataStore.edit { prefs ->
            prefs[DISCLAIMER_SEEN_KEY] = true
        }
    }
}