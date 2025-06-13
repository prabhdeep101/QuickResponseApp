package com.example.quickresponseapp.profile

class ProfileRepository(private val dao: ProfileDao) {
    // Saves or updates user profile in database
    suspend fun saveProfile(profile: UserProfile) = dao.insertProfile(profile)
    // Retrieves the saved user profile from database
    suspend fun getProfile(): UserProfile? = dao.getProfile()
    // clears user profile from database
    suspend fun clearProfile() = dao.clearProfile()
}