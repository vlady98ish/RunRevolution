package com.example.runrevolution.data.repository

import com.example.runrevolution.data.db.RunDetailDAO
import com.example.runrevolution.domain.model.RunDetails
import com.example.runrevolution.domain.repository.RunDetailsRepository
import javax.inject.Inject

class RunDetailsRepositoryImpl @Inject constructor(
    private val runDetailsDAO: RunDetailDAO
) : RunDetailsRepository {
    override suspend fun saveRunDetails(runDetails: RunDetails) {
        runDetailsDAO.addRunDetails(runDetails)
    }

    override suspend fun getRunDetails(): List<RunDetails> {
        return runDetailsDAO.getAll()
    }
}