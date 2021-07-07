package com.app.entertainment.movies.networkcalls

import com.app.weather.data.remote.CurrentWeatherModel
import com.app.weather.utils.API_KEY
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiHelper {

    @GET("/data/2.5/weather")
    fun getCurrentWeatherData(@Query("lat") lat: Double,@Query("lon")long:Double,@Query("units") unit:String = "metric",@Query("appid") apiKey:String = API_KEY): Call<CurrentWeatherModel>
}