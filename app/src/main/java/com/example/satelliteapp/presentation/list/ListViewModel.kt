package com.example.satelliteapp.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.satelliteapp.di.IoDispatcher
import com.example.satelliteapp.domain.model.Satellite
import com.example.satelliteapp.domain.use_case.GetSatelliteList
import com.example.satelliteapp.util.GenericUIState
import com.example.satelliteapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class ListViewModel @Inject constructor(
    private val getSatelliteList: GetSatelliteList,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _searchText = MutableStateFlow("")
    private val initialDelay: Long = 0L

    private val _state = MutableStateFlow(GenericUIState<List<Satellite>>())
    val state = _searchText
        .debounce(1000)
        .combine(_state) { text, state ->
            if (text.isBlank()) {
                state
            } else {
                GenericUIState(data = state.data?.filter {
                    it.name.contains(text, ignoreCase = true)
                })
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            GenericUIState(isLoading = true)
        )

    init {
        viewModelScope.launch(ioDispatcher) {
            _state.emit(GenericUIState(isLoading = true))

            delay(initialDelay)

            when (val list = getSatelliteList()) {
                is Resource.Success -> {
                    _state.emit(GenericUIState(isLoading = false, data = list.data))
                }
                is Resource.Error -> {
                    _state.emit(GenericUIState(isLoading = false, error = list.errorType))
                }
            }
        }
    }

    fun onSearchTextChanged(text: String) {
        _searchText.value = text
    }
}
