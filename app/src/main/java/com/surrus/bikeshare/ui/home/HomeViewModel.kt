package com.surrus.bikeshare.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.surrus.common.remote.CityBikesApi
import com.surrus.common.remote.Station
import com.surrus.common.repository.CityBikesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    val stations = MutableLiveData<List<Station>>()
    private val cityBikesRepository = CityBikesRepository()

    init {

        Log.d("BikeShare", "starting job")
        val job = viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                //val result = repo.fetchBikeShareInfo("oslo-bysykkel")
                val network = cityBikesRepository.fetchBikeShareInfo("galway")

                if (stations.value != null) {
                    if (stations.value != network.stations) {
                        Log.d("BikeShare", "results changed")
                    }

                    val diff = stations.value?.minus(network.stations)
                    if (!diff.isNullOrEmpty()) {

                        Log.d("BikeShare", "diff found")
                    }


                }
                stations.postValue(network.stations)
                Log.d("BikeShare", "got results")

                delay(5000)
            }
        }

        job.invokeOnCompletion {
            Log.d("BikeShare", "job complated")
        }
    }

    override fun onCleared() {
        super.onCleared()
    }

}