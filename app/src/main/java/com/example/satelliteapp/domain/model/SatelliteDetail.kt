package com.example.satelliteapp.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "satellites"
)
data class SatelliteDetail(
    @PrimaryKey
    val id: Int,
    @SerializedName("cost_per_launch")
    val cost: Int,
    @SerializedName("first_flight")
    val date: String,
    val height: Int,
    val mass: Int
)