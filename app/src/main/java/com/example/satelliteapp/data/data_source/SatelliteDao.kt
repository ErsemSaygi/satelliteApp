package com.example.satelliteapp.data.data_source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.satelliteapp.domain.model.SatelliteDetail

@Dao
interface SatelliteDao {

    @Insert
    suspend fun add(satellite: SatelliteDetail)

    @Query("SELECT * FROM satellites WHERE id=:id")
    suspend fun get(id:Int): SatelliteDetail
}