package com.example.quickresponseapp.contacts

import androidx.lifecycle.LiveData
import androidx.room.*

// Database operations for Contact table
@Dao
interface ContactDao {
    // Get all contacts as LiveData, ordered by default then alphabetically
    @Query("SELECT * FROM contact ORDER BY isDefault DESC, name ASC")
    fun getAllContacts(): LiveData<List<Contact>>

    // Get all contacts once, ordered by default then alphabetically
    @Query("SELECT * FROM contact ORDER BY isDefault DESC, name ASC")
    suspend fun getAllContactsOnce(): List<Contact>

    // Get the default contact, only one
    @Query("SELECT * FROM contact WHERE isDefault = 1 LIMIT 1")
    suspend fun getDefaultContact(): Contact?

    // Insert or update a contact
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: Contact)

    // Delete a contact
    @Delete
    suspend fun deleteContact(contact: Contact)

    // Get contact by ID as LiveData
    @Query("SELECT * FROM contact WHERE id = :id LIMIT 1")
    fun getContactById(id: Int): LiveData<Contact>
}