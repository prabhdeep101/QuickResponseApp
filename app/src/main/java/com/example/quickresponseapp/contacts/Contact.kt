package com.example.quickresponseapp.contacts

import androidx.room.PrimaryKey

data class Contact(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val relation: String,
    val isDefault: Boolean,
    val profileImageUri: String? = null
)