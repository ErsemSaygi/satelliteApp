package com.example.satelliteapp.domain.use_case

import android.content.Context
import com.example.satelliteapp.domain.model.SatelliteDetail
import com.example.satelliteapp.domain.repository.SatelliteRepository
import com.example.satelliteapp.util.Resource

class GetSatelliteDetail(
    private val repository: SatelliteRepository,
    private val context: Context
) {
    suspend operator fun invoke(id: Int): Resource<SatelliteDetail> {
        return repository.getSatelliteDetail(context,id)
    }
}