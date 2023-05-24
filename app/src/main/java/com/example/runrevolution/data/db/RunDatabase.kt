package com.example.runrevolution.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.runrevolution.domain.model.LocationDetails
import com.example.runrevolution.domain.model.RunDetails

@Database(
    entities = [RunDetails::class, LocationDetails::class],
    version = 2
)
@TypeConverters(Converters::class)
abstract class RunDatabase : RoomDatabase() {

    abstract fun getRunDetailsDao(): RunDetailDAO
    abstract fun getLocationDao(): LocationDAO


}