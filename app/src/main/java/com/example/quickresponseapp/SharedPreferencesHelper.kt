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

// Create datastore instance
val Context.dataStore by preferencesDataStore("app_prefs")
// Object for managing preferences
object AppPreferences {
    // Store the language and disclaimer flag
    private val LANG_KEY = stringPreferencesKey("app_lang")
    private val DISCLAIMER_SEEN_KEY = booleanPreferencesKey("disclaimer_seen")

    // Language Preference as a flow
    fun getLanguageFlow(context: Context): Flow<String> {
        return context.dataStore.data.map { prefs ->
            prefs[LANG_KEY] ?: "en"
        }
    }
    // Get the saved language
    suspend fun getLanguage(context: Context): String {
        return context.dataStore.data.first()[LANG_KEY] ?: "en"
    }
    // Save the selected language
    suspend fun setLanguage(context: Context, lang: String) {
        context.dataStore.edit { prefs ->
            prefs[LANG_KEY] = lang
        }
    }
    // Apply the saved locale to the context (used when attaching a fragment)
    fun applySavedLocale(context: Context): Context {
        val langCode = runBlocking {
            context.dataStore.data.first()[LANG_KEY] ?: "en"
        }
        return updateLocale(context, langCode)
    }
    // Update context with new locale (language)
    fun updateLocale(context: Context, langCode: String): Context {
        val locale = Locale(langCode)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)

        // Return updated context
        return context.createConfigurationContext(config)
    }

    // Observe if disclaimer has been seen
    fun hasSeenDisclaimer(context: Context): Flow<Boolean> {
        return context.dataStore.data.map { prefs ->
            prefs[DISCLAIMER_SEEN_KEY] ?: false
        }
    }
    // Mark the disclaimer as seen
    suspend fun setDisclaimerSeen(context: Context) {
        context.dataStore.edit { prefs ->
            prefs[DISCLAIMER_SEEN_KEY] = true
        }
    }
}