package com.app.weather.data.remote;

import com.google.gson.annotations.SerializedName;

public class CurrentWeatherModel {
    @SerializedName("weather")
    private Weather[] weathers;
    @SerializedName("main")
    private Main main;


    public Main getMain() {
        return main;
    }

    public Weather[] getWeathers() {
        return weathers;
    }
}
