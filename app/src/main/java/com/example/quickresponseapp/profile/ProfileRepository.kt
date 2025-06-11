package com.example.quickresponseapp.profile

class ProfileRepository(private val dao: ProfileDao) {
    suspend fun saveProfile(profile: UserProfile) = dao.insertProfile(profile)
    suspend fun getProfile(): UserProfile? = dao.getProfile()
    suspend fun clearProfile() = dao.clearProfile()
}