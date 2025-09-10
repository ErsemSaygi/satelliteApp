package com.example.satelliteapp.data.repository

import android.content.Context
import com.example.satelliteapp.R
import com.example.satelliteapp.data.data_source.SatelliteDao
import com.example.satelliteapp.data.model.Root
import com.example.satelliteapp.domain.model.Coordinate
import com.example.satelliteapp.domain.model.Position
import com.example.satelliteapp.domain.model.Satellite
import com.example.satelliteapp.domain.model.SatelliteDetail
import com.example.satelliteapp.domain.repository.SatelliteRepository
import com.example.satelliteapp.util.Constants.Companion.SATELLITE_DETAIL_JSON_NAME
import com.example.satelliteapp.util.Constants.Companion.SATELLITE_JSON_NAME
import com.example.satelliteapp.util.Constants.Companion.SATELLITE_POSITIONS_JSON_NAME
import com.example.satelliteapp.util.Resource
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject

class SatelliteRepositoryImpl @Inject constructor(private val dao: SatelliteDao): SatelliteRepository {
    override suspend fun getSatelliteList(context: Context): Resource<List<Satellite>> {
        return try {
            val jsonString = context.assets.open(SATELLITE_JSON_NAME).bufferedReader().use {
                it.readText()
            }
            val result:List<Satellite> = Gson().fromJson(jsonString, object: TypeToken<List<Satellite>>() {}.type)
            Resource.Success(result)
        } catch (io: IOException) {
            Resource.Error(io.message)
        } catch (json: JsonSyntaxException) {
            Resource.Error(json.message)
        } catch (e: java.lang.Exception) {
            Resource.Error(e.message)
        }
    }

    override suspend fun getSatelliteDetail(context: Context, id: Int): Resource<SatelliteDetail> {
        return try {
            val jsonString = context.assets.open(SATELLITE_DETAIL_JSON_NAME).bufferedReader().use {
                it.readText()
            }
            val result:List<SatelliteDetail> = Gson().fromJson(jsonString, object: TypeToken<List<SatelliteDetail>>() {}.type)
            result.find { it.id == id }.let {
                if (it == null) {
                    Resource.Error(context.getString(R.string.id_error))
                }
                else {
                    Resource.Success(it)
                }
            }
        } catch (io: IOException) {
            Resource.Error(io.message)
        } catch (json: JsonSyntaxException) {
            Resource.Error(json.message)
        } catch (e: java.lang.Exception) {
            Resource.Error(e.message)
        }
    }

    override suspend fun addSatelliteDetailToCache(satellite: SatelliteDetail) {
        dao.add(satellite)
    }

    override suspend fun getPositionList(context: Context): Resource<List<Position>> {
        return try {
            val jsonString = context.assets.open(SATELLITE_POSITIONS_JSON_NAME).bufferedReader().use {
                it.readText()
            }
            val result = Gson().fromJson<Root>(jsonString, object : TypeToken<Root>() {}.type)

            val positionList = mutableListOf<Position>()
            result.list.forEach {
                val coordinateList = mutableListOf<Coordinate>()
                it.positions.forEach { pos ->
                    coordinateList.add(Coordinate(pos.posX,pos.posY))
                }
                positionList.add(Position(it.id,coordinateList))
            }
            Resource.Success(positionList)
        } catch (io: IOException) {
            Resource.Error(io.message)
        } catch (json: JsonSyntaxException) {
            Resource.Error(json.message)
        } catch (e: java.lang.Exception) {
            Resource.Error(e.message)
        }
    }



    override suspend fun getSatelliteFromCache(id: Int): Resource<SatelliteDetail> {
        return try {
            val result = dao.get(id)
            if (result == null) {   //returns null when specified id isn't in the table
                Resource.Error("")
            } else {
                Resource.Success(result)
            }
        } catch (e: Exception) {
            Resource.Error(e.message)
        }
    }
}