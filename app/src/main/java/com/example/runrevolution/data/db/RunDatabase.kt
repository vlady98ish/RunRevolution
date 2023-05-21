package com.example.runrevolution.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.runrevolution.domain.model.RunDetails

@Database(
    entities = [RunDetails::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class RunDatabase : RoomDatabase() {

    abstract fun getRunDetailsDao(): RunDetailDAO


}