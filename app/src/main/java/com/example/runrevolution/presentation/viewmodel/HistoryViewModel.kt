package com.example.runrevolution.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runrevolution.domain.model.RunDetails
import com.example.runrevolution.domain.repository.RunDetailsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val runDetailsRepository: RunDetailsRepository
) : ViewModel()  {

    private val _runningDetails: MutableLiveData<List<RunDetails>> = MutableLiveData()
    val runningDetails: LiveData<List<RunDetails>> = _runningDetails


    init {
        getAllRunDetails()
    }

    private fun getAllRunDetails() {
        viewModelScope.launch(Dispatchers.IO){
            val runDetails = runDetailsRepository.getRunDetails()
            _runningDetails.postValue(runDetails)
        }

    }
}