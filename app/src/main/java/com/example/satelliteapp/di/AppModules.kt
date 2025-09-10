package com.example.satelliteapp.di

import android.content.Context
import androidx.room.Room
import com.example.satelliteapp.data.data_source.SatelliteDatabase
import com.example.satelliteapp.data.repository.SatelliteRepositoryImpl
import com.example.satelliteapp.domain.repository.SatelliteRepository
import com.example.satelliteapp.domain.use_case.AddSatelliteDetailToCache
import com.example.satelliteapp.domain.use_case.GetPositionList
import com.example.satelliteapp.domain.use_case.GetSatelliteDetail
import com.example.satelliteapp.domain.use_case.GetSatelliteDetailFromCache
import com.example.satelliteapp.domain.use_case.GetSatelliteList
import com.example.satelliteapp.presentation.list.SatelliteListAdapter
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): SatelliteDatabase {
        return Room.databaseBuilder(
            context,
            SatelliteDatabase::class.java,
            "satellite_db"
        ).build()
    }

    @Provides
    fun provideDao(db: SatelliteDatabase) = db.getSatelliteDao()
}

@Module
@InstallIn(SingletonComponent::class)
object AdapterModule {

    @Singleton
    @Provides
    fun provideSatelliteAdapter(): SatelliteListAdapter = SatelliteListAdapter()
}
@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideGetSatelliteDetail(
        repository: SatelliteRepository,
        @ApplicationContext context: Context
    ): GetSatelliteDetail {
        return GetSatelliteDetail(repository, context)
    }

    // diğer use case'ler de aynı şekilde:
    @Provides
    @Singleton
    fun provideGetSatelliteList(
        repository: SatelliteRepository,
        @ApplicationContext context: Context
    ): GetSatelliteList {
        return GetSatelliteList(repository, context)
    }

    @Provides
    @Singleton
    fun provideGetSatelliteDetailFromCache(
        repository: SatelliteRepository
    ): GetSatelliteDetailFromCache {
        return GetSatelliteDetailFromCache(repository)
    }

    @Provides
    @Singleton
    fun provideAddSatelliteDetailToCache(
        repository: SatelliteRepository
    ): AddSatelliteDetailToCache {
        return AddSatelliteDetailToCache(repository)
    }

    @Provides
    @Singleton
    fun provideGetPositionList(
        repository: SatelliteRepository,
        @ApplicationContext context: Context
    ): GetPositionList {
        return GetPositionList(repository, context)
    }
}
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindSatelliteRepository(
        impl: SatelliteRepositoryImpl
    ): SatelliteRepository
}

@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {

    @Provides
    @IoDispatcher
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

}
