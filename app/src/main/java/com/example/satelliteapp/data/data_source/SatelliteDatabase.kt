package com.example.satelliteapp.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.satelliteapp.domain.model.SatelliteDetail


@Database(entities = [SatelliteDetail::class], version = 1)
abstract class SatelliteDatabase: RoomDatabase() {
    abstract fun getSatelliteDao(): SatelliteDao
}