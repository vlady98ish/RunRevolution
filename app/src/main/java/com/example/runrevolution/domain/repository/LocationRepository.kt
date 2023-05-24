package com.example.runrevolution.domain.repository


import com.example.runrevolution.domain.model.LocationDetails
import kotlinx.coroutines.flow.Flow

interface LocationRepository {

    suspend fun getLocations(): List<LocationDetails>

    suspend fun updateLocation(location: LocationDetails)

    suspend fun deleteLocations()

    fun getLatestLocation(): Flow<List<LocationDetails>>
}