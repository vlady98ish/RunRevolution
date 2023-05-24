package com.example.runrevolution.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.runrevolution.domain.model.RunDetails
import com.example.runrevolution.utils.other.Constant.RUNNING_TABLE

@Dao
interface RunDetailDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRunDetails(runDetails: RunDetails)


    @Delete
    suspend fun deleteRunDetail(runDetails: RunDetails)

    @Query("SELECT * FROM $RUNNING_TABLE")
    fun getAll(): LiveData<List<RunDetails>>

}