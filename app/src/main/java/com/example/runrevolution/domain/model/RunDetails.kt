package com.example.runrevolution.domain.model

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.runrevolution.utils.other.Constant.RUNNING_TABLE


@Entity(tableName = RUNNING_TABLE)
data class RunDetails(
    var mapSnapShot: Bitmap? = null,
    var timeDate: Long = 0L,
    var avgSpeed: Float = 0f,
    var distance: Float = 0f,
    var time: Long = 0L,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}