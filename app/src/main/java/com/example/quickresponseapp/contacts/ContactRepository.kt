package com.example.quickresponseapp.contacts

import androidx.lifecycle.LiveData

// Repository for data access to the rest of the app (access contacts in messages)
class ContactRepository(private val contactDao: ContactDao) {

    // LiveData all contacts
    fun getAllContacts(): LiveData<List<Contact>> {
        return contactDao.getAllContacts()
    }

    // Insert new contact or updates existing contact
    suspend fun insert(contact: Contact) {
        contactDao.insertContact(contact)
    }

    // Delete contact from database
    suspend fun delete(contact: Contact) {
        contactDao.deleteContact(contact)
    }

    // Get default contact if exists
    suspend fun getDefaultContact(): Contact? {
        return contactDao.getDefaultContact()
    }
}