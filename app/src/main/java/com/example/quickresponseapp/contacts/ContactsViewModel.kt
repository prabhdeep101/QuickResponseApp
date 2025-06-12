package com.example.quickresponseapp.contacts

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val contactDao: ContactDao
) : ViewModel() {

    val contacts: LiveData<List<Contact>> = contactDao.getAllContacts()

    fun insert(contact: Contact) = viewModelScope.launch {
        contactDao.insertContact(contact)
    }

    fun delete(contact: Contact) = viewModelScope.launch {
        contactDao.deleteContact(contact)
    }

    suspend fun getDefaultContact(): Contact? {
        return contactDao.getDefaultContact()
    }
}