package com.example.quickresponseapp.profile

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dagger.Module

// Defines database for storing user profile
@Database(entities = [UserProfile::class], version = 1)
// Used to deal with Date
@TypeConverters(Converters::class)
abstract class ProfileDatabase : RoomDatabase() {
    // Access to Dao functions
    abstract fun profileDao(): ProfileDao

    companion object {
        // Keeps instance of database
        @Volatile
        private var INSTANCE: ProfileDatabase? = null

        // Returns singleton instance of database
        fun getDatabase(context: Context): ProfileDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ProfileDatabase::class.java,
                    "profile_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}