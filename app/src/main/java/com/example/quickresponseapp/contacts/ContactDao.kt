package com.example.quickresponseapp.contacts

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ContactDao {
    @Query("SELECT * FROM contact ORDER BY isDefault DESC, name ASC")
    fun getAllContacts(): LiveData<List<Contact>>

    @Query("SELECT * FROM contact ORDER BY isDefault DESC, name ASC")
    suspend fun getAllContactsOnce(): List<Contact>

    @Query("SELECT * FROM contact WHERE isDefault = 1 LIMIT 1")
    suspend fun getDefaultContact(): Contact?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: Contact)

    @Delete
    suspend fun deleteContact(contact: Contact)

    @Query("SELECT * FROM contact WHERE id = :id LIMIT 1")
    fun getContactById(id: Int): LiveData<Contact>
}