package com.example.quickresponseapp.profile

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
object ProfileDatabaseModule {

    // Provides a singleton instance of profile database
    @Provides
    @Singleton
    fun provideProfileDatabase(@ApplicationContext appContext: Context): ProfileDatabase {
        return Room.databaseBuilder(
            appContext,
            ProfileDatabase::class.java,
            "profile_database"
        ).fallbackToDestructiveMigration().build()
    }

    // Provides instance of ProfileDao using the profile database
    @Provides
    fun provideProfileDao(db: ProfileDatabase): ProfileDao {
        return db.profileDao()
    }
}