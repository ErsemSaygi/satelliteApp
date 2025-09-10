package com.example.satelliteapp

import com.example.satelliteapp.domain.model.Satellite
import com.example.satelliteapp.domain.use_case.GetSatelliteList
import com.example.satelliteapp.presentation.list.ListViewModel
import com.example.satelliteapp.util.Resource
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@OptIn(ExperimentalCoroutinesApi::class)
class SatelliteUnitTest {

    private lateinit var getSatelliteList: GetSatelliteList
    private lateinit var viewModel: ListViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        getSatelliteList = mockk()
    }

    @Test
    fun `when init success then state contains data`() = runTest {
        // Given
        val satellites = listOf(
            Satellite(id = 1, name = "Hubble", active = true),
            Satellite(id = 2, name = "James Webb", active = false)
        )
        coEvery { getSatelliteList() } returns Resource.Success(satellites)

        // When
        viewModel = ListViewModel(getSatelliteList, mainDispatcherRule.testDispatcher)
        advanceUntilIdle()
        // Then
        val state = viewModel.state.drop(1).first()
        assertFalse(state.isLoading)
        assertEquals(2, state.data?.size)
        assertEquals("Hubble", state.data?.first()?.name)
    }

    @Test
    fun `when init error then state contains error`() = runTest {
        // Given
        coEvery { getSatelliteList() } returns Resource.Error("Network error")

        // When
        viewModel = ListViewModel(getSatelliteList, StandardTestDispatcher(testScheduler))

        // Then
        val state = viewModel.state.drop(1).first()
        assertFalse(state.isLoading)
        assertEquals("Network error", state.error)
    }

    @Test
    fun `when search text changes then data is filtered`() = runTest {
        // Given
        val satellites = listOf(
            Satellite(1,true, "Hubble" ),
            Satellite(2,true, "James Webb" )
        )
        coEvery { getSatelliteList() } returns Resource.Success(satellites)

        // When
        viewModel = ListViewModel(getSatelliteList, StandardTestDispatcher(testScheduler))
        advanceUntilIdle()

        viewModel.onSearchTextChanged("Hubble")

        // Then
        val state = viewModel.state.drop(1).first()
        assertEquals(1, state.data?.size)
        assertEquals("Hubble", state.data?.first()?.name)
    }
}
@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    val testDispatcher: TestDispatcher = StandardTestDispatcher()
) : TestWatcher(), TestRule {

    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}