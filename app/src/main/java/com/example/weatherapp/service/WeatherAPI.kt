package com.example.weatherapp.service

import com.example.weatherapp.model.WeatherModel
import com.example.weatherapp.util.Constants.END_POINT
import com.example.weatherapp.util.Constants.QUERY
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {
    @GET(END_POINT)
    fun getData(
        @Query(QUERY) cityName: String
    ): Single<WeatherModel>
}