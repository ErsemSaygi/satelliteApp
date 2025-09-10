package com.example.satelliteapp.domain.use_case

import com.example.satelliteapp.domain.model.SatelliteDetail
import com.example.satelliteapp.domain.repository.SatelliteRepository


class AddSatelliteDetailToCache(private val repository: SatelliteRepository) {
    suspend operator fun invoke(satellite: SatelliteDetail) {
        return repository.addSatelliteDetailToCache(satellite)
    }
}