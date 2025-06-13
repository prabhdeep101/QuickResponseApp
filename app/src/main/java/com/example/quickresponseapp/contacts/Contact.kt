package com.example.quickresponseapp.contacts

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
// Room entity with table name contact
@Entity(tableName = "contact")
data class Contact(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val address: String,
    val phone: String,
    val relation: String,
    val isDefault: Boolean = false,
    val isOrangaTamarikiApproved: Boolean = false,
    val photoUri: String? = null
) : Parcelable // Allows data to be passed in bundles