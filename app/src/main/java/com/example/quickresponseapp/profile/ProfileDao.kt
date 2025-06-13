package com.example.quickresponseapp.profile

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProfileDao {
    // Retrieves user profile in database
    @Query("SELECT * FROM profile LIMIT 1")
    suspend fun getProfile(): UserProfile?

    // Inserts profile into database, replacing if one already exists
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: UserProfile)

    // Deletes all profiles from database
    @Query("DELETE FROM profile")
    suspend fun clearProfile()
}