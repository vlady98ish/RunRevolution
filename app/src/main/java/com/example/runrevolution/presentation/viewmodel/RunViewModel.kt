package com.example.runrevolution.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runrevolution.domain.model.LocationDetails
import com.example.runrevolution.domain.model.RunDetails
import com.example.runrevolution.domain.repository.LocationRepository
import com.example.runrevolution.domain.repository.RunDetailsRepository
import com.example.runrevolution.presentation.service.RunningService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RunViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val runDetailsRepository: RunDetailsRepository
) : ViewModel() {


    private val _locationPoints: MutableLiveData<List<LocationDetails>> = MutableLiveData()
    val locationPoints: LiveData<List<LocationDetails>> = _locationPoints
    private val _timeInSeconds: MutableLiveData<Long> = RunningService.timeInSeconds
    val timeInSeconds: LiveData<Long> = _timeInSeconds
    private val _distance: MutableLiveData<Float> = RunningService.totalDistance
    val distance: LiveData<Float> = _distance


    private val _speed: MutableLiveData<Float> = RunningService.currentSpeed
    val speed: LiveData<Float> = _speed

    init {
        getAllLocations()
        observeLocations()
    }

    val runningState: MutableStateFlow<Boolean> = RunningService.isRunning


    private fun observeLocations() {
        locationRepository.getLatestLocation()
            .onEach { locations ->
                _locationPoints.value = _locationPoints.value.orEmpty().toMutableList().apply {
                    addAll(locations)
                }
            }
            .launchIn(viewModelScope)
    }

    fun deleteAllLocations() {
        viewModelScope.launch {
            locationRepository.deleteLocations()
        }
    }

    fun clearPoints() {
        _locationPoints.value = mutableListOf()
        _timeInSeconds.value = 0L
        _distance.value = 0f
    }

    fun getAllLocations() {
        viewModelScope.launch {
            val locations = locationRepository.getLocations()
            _locationPoints.postValue(locations)

        }

    }

    fun saveRunDetails(runDetails: RunDetails) {
        viewModelScope.launch {
            runDetailsRepository.saveRunDetails(runDetails)
        }
    }


}