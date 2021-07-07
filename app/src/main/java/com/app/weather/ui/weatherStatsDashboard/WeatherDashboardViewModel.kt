package com.app.weather.ui.weatherStatsDashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.entertainment.movies.repository.OnResponseSucceeded
import com.app.entertainment.movies.repository.RemoteWeatherRepo
import com.app.weather.data.remote.CurrentWeatherModel

class WeatherDashboardViewModel : ViewModel() {
    var currentWeatherModel: MutableLiveData<CurrentWeatherModel> = MutableLiveData()

    fun getCurrentLocationWeather(lat: Double, long: Double) {
        RemoteWeatherRepo().getCurrentLocationWeatherData(lat, long, object : OnResponseSucceeded {
            override fun response(responseObject: Any?) {
                currentWeatherModel.value = responseObject as CurrentWeatherModel
            }
        })
    }
}