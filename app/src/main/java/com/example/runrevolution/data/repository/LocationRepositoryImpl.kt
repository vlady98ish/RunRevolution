package com.example.runrevolution.data.repository

import androidx.annotation.WorkerThread
import com.example.runrevolution.data.db.LocationDAO
import com.example.runrevolution.domain.model.LocationDetails
import com.example.runrevolution.domain.repository.LocationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val locationDAO: LocationDAO
) : LocationRepository {
    override suspend fun getLocations(): List<LocationDetails> {
        return locationDAO.getLocationsDetails()
    }

    @WorkerThread
    override suspend fun updateLocation(location: LocationDetails) {
        locationDAO.updateLocationDetails(location)
    }

    override suspend fun deleteLocations() {
        locationDAO.deleteLocationDetails()
    }

    override fun getLatestLocation(): Flow<List<LocationDetails>> {
        return locationDAO.getLatestLocation()
    }


}