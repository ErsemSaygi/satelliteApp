package com.example.satelliteapp.domain.use_case

import android.content.Context
import com.example.satelliteapp.domain.model.Satellite
import com.example.satelliteapp.domain.repository.SatelliteRepository
import com.example.satelliteapp.util.Resource

class GetSatelliteList(private val repository: SatelliteRepository, private val context: Context) {
    suspend operator fun invoke(): Resource<List<Satellite>> {
        return repository.getSatelliteList(context)
    }
}