package com.example.quickresponseapp.contacts

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Declare Room database with contact entity and version number
@Database(entities = [Contact::class], version = 2)
abstract class ContactDatabase : RoomDatabase() {
    // Access the DAO
    abstract fun contactDao(): ContactDao

    companion object {
        // Volatile variable to access the instance
        @Volatile
        private var INSTANCE: ContactDatabase? = null

        // Returns singleton instance of database
        fun getDatabase(context: Context): ContactDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ContactDatabase::class.java,
                    "contact_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
