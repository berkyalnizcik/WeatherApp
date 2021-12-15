package com.example.weatherapp.view

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.util.Constants.CITY_DEF
import com.example.weatherapp.util.Constants.CITY_NAME
import com.example.weatherapp.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var get: SharedPreferences
    private lateinit var set: SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        get = getSharedPreferences(packageName, MODE_PRIVATE)
        set = get.edit()
        viewModel = ViewModelProviders.of(this@MainActivity).get(MainViewModel::class.java)
        val cName = get.getString(CITY_NAME, CITY_DEF)
        binding.edtCityName.setText(cName)
        viewModel.refreshData(cName!!)
        getLiveData()
        with(binding) {
            swipeRefreshLayout.setOnRefreshListener {
                llDataView.visibility = View.GONE
                tvError.visibility = View.GONE
                pbLoading.visibility = View.GONE
                val cityName = get.getString(CITY_NAME, cName)
                edtCityName.setText(cityName)
                viewModel.refreshData(cityName!!)
                swipeRefreshLayout.isRefreshing = false
            }
            imgSearchCityName.setOnClickListener {
                val cityName = edtCityName.text.toString()
                set.putString(CITY_NAME, cityName)
                set.apply()
                viewModel.refreshData(cityName)
                getLiveData()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getLiveData() {
        viewModel.weather_data.observe(this, { data ->
            data?.let {
                with(binding) {
                    llDataView.visibility = View.VISIBLE
                    pbLoading.visibility = View.GONE
                    tvDegree.text = data.main.temp.toString() + "°C"
                    tvCountryCode.text = data.sys.country
                    tvCityName.text = data.name
                    tvHumidity.text = data.main.humidity.toString()
                    tvSpeed.text = data.wind.speed.toString() + "%"
                    tvLat.text = data.coord.lat.toString()
                    tvLon.text = data.coord.lon.toString()
                    Glide.with(this@MainActivity)
                        .load("http://openweathermap.org/img/wn/" + data.weather[0].icon + "@2x.png")
                        .into(imgWeatherIcon)
                }
            }
        })
        viewModel.weather_loading.observe(this, { load ->
            load?.let {
                with(binding) {
                    if (it) {
                        pbLoading.visibility = View.VISIBLE
                        tvError.visibility = View.GONE
                        llDataView.visibility = View.GONE
                    } else {
                        pbLoading.visibility = View.GONE
                    }
                }
            }
        })
        viewModel.weather_error.observe(this, { error ->
            error?.let {
                with(binding) {
                    if (it) {
                        tvError.visibility = View.VISIBLE
                        llDataView.visibility = View.GONE
                        pbLoading.visibility = View.GONE
                    } else {
                        tvError.visibility = View.GONE
                    }
                }
            }
        })
    }
}