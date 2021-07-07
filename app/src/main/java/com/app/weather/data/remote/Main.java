package com.app.weather.data.remote;

import com.google.gson.annotations.SerializedName;

public class Main {
    @SerializedName("temp")
    private float temp;
    @SerializedName("feels_like")
    private float feelsLike;
    @SerializedName("temp_min")
    private float minTemp;
    @SerializedName("temp_max")
    private float maxTemp;

    public float getTemp() {
        return temp;
    }

    public float getFeelsLike() {
        return feelsLike;
    }

    public float getMaxTemp() {
        return maxTemp;
    }

    public float getMinTemp() {
        return minTemp;
    }
}
