package com.app.entertainment.movies.repository

import android.util.Log
import com.app.entertainment.movies.networkcalls.ApiClient
import com.app.entertainment.movies.networkcalls.ApiHelper
import com.app.weather.data.remote.CurrentWeatherModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * centralized repository class that
 */
 class RemoteWeatherRepo {
    val TAG = "RemoteWeatherRepo"
    fun getCurrentLocationWeatherData(
        lat: Double,
        long: Double,
        onResponseSucceeded: OnResponseSucceeded
    ) {
        val apiService = ApiClient.getClient().create(ApiHelper::class.java)
        val call = apiService.getCurrentWeatherData(lat, long)
        call.enqueue(object : Callback<CurrentWeatherModel> {

            override fun onResponse(
                call: Call<CurrentWeatherModel>,
                response: Response<CurrentWeatherModel>
            ) {
                if (response.isSuccessful) {
                    Log.d(TAG, response.body().toString())
                    onResponseSucceeded.response(response.body())
                } else
                    Log.d(TAG, response.errorBody().toString())
            }

            override fun onFailure(call: Call<CurrentWeatherModel>, throwable: Throwable) {
                Log.e(TAG, "Error Message: " + throwable.localizedMessage)
            }
        })
    }
}