package com.example.runrevolution.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.runrevolution.utils.other.Constant.LOCATION_TABLE

@Entity(tableName = LOCATION_TABLE)
class LocationDetails(
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    val time: Long

) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

}