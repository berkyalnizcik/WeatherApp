package com.berk.weatherapp.service

import com.berk.weatherapp.model.WeatherModel
import com.berk.weatherapp.util.Constants.END_POINT
import com.berk.weatherapp.util.Constants.QUERY
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {
    @GET(END_POINT)
    fun getData(
        @Query(QUERY) cityName: String
    ): Single<WeatherModel>
}