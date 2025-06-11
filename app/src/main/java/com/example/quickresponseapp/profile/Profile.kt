package com.example.quickresponseapp.profile

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profile")
data class UserProfile(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val address: String,
    val guardianName: String,
    val guardianPhone: String,
    val orangaAppointed: Boolean,
    val profileImagePath: String
)