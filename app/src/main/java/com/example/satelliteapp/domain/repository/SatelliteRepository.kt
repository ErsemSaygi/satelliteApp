package com.example.satelliteapp.domain.repository

import android.content.Context
import com.example.satelliteapp.domain.model.Position
import com.example.satelliteapp.domain.model.Satellite
import com.example.satelliteapp.domain.model.SatelliteDetail
import com.example.satelliteapp.util.Resource

interface SatelliteRepository {
    suspend fun getSatelliteList(context: Context): Resource<List<Satellite>>
    suspend fun getSatelliteDetail(context: Context, id: Int): Resource<SatelliteDetail>
    suspend fun addSatelliteDetailToCache(satellite: SatelliteDetail)
    suspend fun getSatelliteFromCache(id: Int): Resource<SatelliteDetail>
    suspend fun getPositionList(context: Context): Resource<List<Position>>
}