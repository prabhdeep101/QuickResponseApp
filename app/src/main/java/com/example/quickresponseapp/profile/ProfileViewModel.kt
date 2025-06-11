package com.example.quickresponseapp.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileDao: ProfileDao
) : ViewModel() {

    fun saveProfile(profile: UserProfile) = viewModelScope.launch {
        profileDao.insertProfile(profile)
    }

    fun getProfile(onLoaded: (UserProfile?) -> Unit) = viewModelScope.launch {
        onLoaded(profileDao.getProfile())
    }
}