package com.example.quickresponseapp.messages

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quickresponseapp.contacts.Contact
import com.example.quickresponseapp.contacts.ContactDao
import com.example.quickresponseapp.contacts.ContactDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessagesViewModel @Inject constructor(
    private val contactDao: ContactDao
) : ViewModel() {
    val contacts: LiveData<List<Contact>> = contactDao.getAllContacts()
}
