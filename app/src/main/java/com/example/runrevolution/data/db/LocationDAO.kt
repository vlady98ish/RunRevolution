package com.example.runrevolution.data.db


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.runrevolution.domain.model.LocationDetails
import com.example.runrevolution.utils.other.Constant.LOCATION_TABLE
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDAO {


    @Transaction
    suspend fun updateLocationDetails(location: LocationDetails) {
        insertLocationDetails(location)

    }


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLocationDetails(location: LocationDetails)

    @Query("DELETE FROM $LOCATION_TABLE")
    suspend fun deleteLocationDetails()

    @Query("SELECT * FROM $LOCATION_TABLE")
    suspend fun getLocationsDetails(): List<LocationDetails>

    @Query("SELECT * FROM $LOCATION_TABLE ORDER BY time DESC LIMIT 1")
    fun getLatestLocation(): Flow<List<LocationDetails>>
}