package com.app.weather.utils

import android.util.Log
import android.widget.ImageView
import com.squareup.picasso.Picasso

class CommonMethods {
    companion object {

        fun renderIconInView(imageSource: String, imageView: ImageView) {
            Log.d("icon", "$ICON_BAES_URL$imageSource@2x.png")
            Picasso.get().load("$ICON_BAES_URL$imageSource@2xx.png").into(imageView)
        }
    }
}