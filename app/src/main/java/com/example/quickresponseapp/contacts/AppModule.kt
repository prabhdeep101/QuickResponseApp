package com.example.quickresponseapp.contacts

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Provides a singleton instance of Room database
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ContactDatabase =
        Room.databaseBuilder(context, ContactDatabase::class.java, "contacts_db")
            .fallbackToDestructiveMigration()
            .build()

    // Provides DAO from database
    @Provides
    fun provideContactDao(db: ContactDatabase): ContactDao = db.contactDao()
}