package com.example.quickresponseapp.contacts

import androidx.room.Database
import androidx.room.RoomDatabase
import dagger.Module

@Database(entities = [Contact::class], version = 1)
abstract class ContactDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao
}