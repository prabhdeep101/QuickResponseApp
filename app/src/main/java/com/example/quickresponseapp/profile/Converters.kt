package com.example.quickresponseapp.profile

import androidx.room.TypeConverter
import java.util.Date

class Converters {
    // Converts a Long timestamp from the database into a Date object
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }
    // Converts a Date object into a Long timestamp for database storage
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}