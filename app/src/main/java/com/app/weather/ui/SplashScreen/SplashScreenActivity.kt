package com.app.weather.ui.SplashScreen

import android.content.Intent
import android.os.Bundle
import com.app.weather.BaseActivity
import com.app.weather.databinding.SplashScreenActivityBinding
import com.app.weather.utils.SPLASH_SCREEN_TIMER
import com.app.weather.ui.weatherStatsDashboard.WeatherDashboardActivity

class SplashScreenActivity : BaseActivity() {
    lateinit var binding: SplashScreenActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SplashScreenActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Thread {
            Thread.sleep(SPLASH_SCREEN_TIMER)
            this.runOnUiThread {
                this.finish()
                startWeatherDashboardActivity()
            }
        }.start()
    }

    /**
     * this method is responsible to start the weather details activity
     */
    private fun startWeatherDashboardActivity() {
        val intent = Intent(this, WeatherDashboardActivity::class.java)
        startActivity(intent)
    }
}