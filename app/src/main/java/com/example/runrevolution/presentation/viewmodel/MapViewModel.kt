package com.example.runrevolution.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runrevolution.domain.model.LocationDetails
import com.example.runrevolution.domain.repository.LocationRepository
import com.example.runrevolution.presentation.service.RunningService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(

    private val locationRepository: LocationRepository
) : ViewModel() {


    private val _locationPoints: MutableLiveData<List<LocationDetails>> = MutableLiveData()
    val locationPoints: LiveData<List<LocationDetails>> = _locationPoints

    val runningState: MutableStateFlow<Boolean> = RunningService.isRunning


    init {
        getAllLocations()
        observeLocations()
    }

    private fun observeLocations() {
        locationRepository.getLatestLocation()
            .onEach { locations ->
                _locationPoints.value = _locationPoints.value.orEmpty().toMutableList().apply {
                    addAll(locations)
                }
            }
            .launchIn(viewModelScope)
    }

    fun getAllLocations() {
        viewModelScope.launch {
            val locations = locationRepository.getLocations()
            _locationPoints.postValue(locations)
        }

    }


}