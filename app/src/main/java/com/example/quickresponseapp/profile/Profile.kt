package com.example.quickresponseapp.profile

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

// data class represents user profile stored in the Room database
@Entity(tableName = "profile")
data class UserProfile(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val birthday: Date,
    val address: String,
    val guardianName: String,
    val guardianPhone: String,
    val orangaAppointed: Boolean,
    val profileImagePath: String
)