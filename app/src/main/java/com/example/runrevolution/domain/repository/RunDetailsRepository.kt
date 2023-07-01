package com.example.runrevolution.domain.repository

import com.example.runrevolution.domain.model.RunDetails

interface RunDetailsRepository {


    suspend fun saveRunDetails(runDetails: RunDetails)

    suspend fun getRunDetails(): List<RunDetails>
}