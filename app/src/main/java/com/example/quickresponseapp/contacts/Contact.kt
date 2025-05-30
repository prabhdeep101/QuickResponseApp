package com.example.quickresponseapp.contacts

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
data class Contact(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val address: String,
    val phone: String,
    val relation: String,
    val isDefault: Boolean = false,
    val isOrangaTamarikiApproved: Boolean = false,
    val profileImageUri: String? = null
)