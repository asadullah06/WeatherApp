package com.app.weather.ui.weatherStatsDashboard

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.app.weather.BaseActivity
import com.app.weather.databinding.WeatherDashboardActiivityBinding
import com.app.weather.data.remote.CurrentWeatherModel
import com.app.weather.utils.CommonMethods
import com.app.weather.utils.LocationFound
import com.app.weather.utils.LocationTracker
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class WeatherDashboardActivity : BaseActivity(), LocationFound {
    lateinit var locationTracker: LocationTracker
     val weatherDashboardViewModel = WeatherDashboardViewModel()
    val REQUEST_CHECK_SETTINGS = 0x1

    lateinit var binding: WeatherDashboardActiivityBinding
    val RREQUEST_CODE = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = WeatherDashboardActiivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        updateDate()
        if (checkPermissions()) {
            locationTracker = LocationTracker()
            locationTracker.startLocationUpdates(this) { loc: Location? -> onLocationFound(loc) }
        } else {
            askPermissionsToUser()
        }
        registerObserver()

    }

    private fun askPermissionsToUser() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                RREQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RREQUEST_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // permission was granted, yay! Do the
                locationTracker = LocationTracker()
                locationTracker.startLocationUpdates(this) { loc: Location? -> onLocationFound(loc) }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                locationTracker.startLocationUpdates(
                    this
                ) { loc: Location? -> onLocationFound(loc) }
            }
        }
    }

    override fun onLocationFound(location: Location?) {
        if (location != null) {
            Toast.makeText(this, "${location.latitude} , ${location.longitude}", Toast.LENGTH_SHORT).show()
            weatherDashboardViewModel.getCurrentLocationWeather(location.latitude,location.longitude)
        }
    }

    private fun registerObserver() {
        weatherDashboardViewModel.currentWeatherModel.observe(this, {
            if (it != null) {
                populateWeatherInfo(it)
            }
        })
    }

    private fun populateWeatherInfo(it: CurrentWeatherModel) {
        val stringBuilder = StringBuilder()
        if (it.weathers.size == 1) {
            binding.currentWeatherDetailsLayout.currentTemperature.text = "${it.main.temp.roundToInt()}"
            stringBuilder.append(it.weathers[0].description).append("\n").append(it.main.minTemp).append("/").append(it.main.maxTemp).append("\n")
                .append(it.main.feelsLike)
            CommonMethods.renderIconInView(it.weathers[0].icon,binding.currentWeatherDetailsLayout.dayNightImageview)
        }
        if (stringBuilder.isNotEmpty())
            binding.currentWeatherDetailsLayout.currentWeatherDetailsTextview.text = stringBuilder
    }

    private fun updateDate() {
        val dateFormat = SimpleDateFormat("MM/dd/yyyy")
        val date = dateFormat.format(Calendar.getInstance().time)
        binding.currentWeatherDetailsLayout.currentDate.text = date
    }

    private fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        return permissionState == PackageManager.PERMISSION_GRANTED
    }
}