package com.example.quickresponseapp.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

// ViewModel for managing user profile data
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileDao: ProfileDao
) : ViewModel() {
    // Saves user profile to database using coroutine
    fun saveProfile(profile: UserProfile) = viewModelScope.launch {
        profileDao.insertProfile(profile)
    }
    // Loads user profile and returns it
    fun getProfile(onLoaded: (UserProfile?) -> Unit) = viewModelScope.launch {
        onLoaded(profileDao.getProfile())
    }
}