package com.example.quickresponseapp.contacts

import androidx.lifecycle.LiveData

class ContactRepository(private val contactDao: ContactDao) {

    fun getAllContacts(): LiveData<List<Contact>> {
        return contactDao.getAllContacts()
    }

    suspend fun insert(contact: Contact) {
        contactDao.insertContact(contact)
    }

    suspend fun delete(contact: Contact) {
        contactDao.deleteContact(contact)
    }

    suspend fun getDefaultContact(): Contact? {
        return contactDao.getDefaultContact()
    }
}