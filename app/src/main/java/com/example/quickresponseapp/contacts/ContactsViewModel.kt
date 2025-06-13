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

    // Live list of all contact
    val contacts: LiveData<List<Contact>> = contactDao.getAllContacts()

    // Insert into database using coroutine
    fun insert(contact: Contact) = viewModelScope.launch {
        contactDao.insertContact(contact)
    }

    // Delete a contact from database using coroutine
    fun delete(contact: Contact) = viewModelScope.launch {
        contactDao.deleteContact(contact)
    }

    // Suspend func to get the default contact
    suspend fun getDefaultContact(): Contact? {
        return contactDao.getDefaultContact()
    }
}