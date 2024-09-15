package eu.example.weatherapi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.example.weatherapi.api.Constant
import eu.example.weatherapi.api.NetworkResponse
import eu.example.weatherapi.api.RetrofitInstance
import eu.example.weatherapi.api.WeatherModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


class WeatherViewModel :ViewModel() {
    private val weatherApi = RetrofitInstance.weatherApi
    private val _weatherResult= MutableLiveData<NetworkResponse<WeatherModel>>()
    val weatherResult:LiveData<NetworkResponse<WeatherModel>> = _weatherResult

    fun getData(city: String) {
        _weatherResult.value= NetworkResponse.Loading

        viewModelScope.launch {
            viewModelScope.launch {
                try {
                    val response = weatherApi.getWeather(Constant.apiKey, city)

                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null) {
                            _weatherResult.value = NetworkResponse.Success(body)
                        } else {
                            _weatherResult.value = NetworkResponse.Error("No data found for the city")
                        }
                    } else {
                        _weatherResult.value = NetworkResponse.Error("Error: ${response.code()} - ${response.message()}")
                    }

                } catch (e: IOException) {
                    _weatherResult.value = NetworkResponse.Error("Network error: ${e.localizedMessage}")
                } catch (e: HttpException) {
                    _weatherResult.value = NetworkResponse.Error("Server error: ${e.localizedMessage}")
                } catch (e: Exception) {
                    _weatherResult.value = NetworkResponse.Error("Unexpected error: ${e.localizedMessage}")
                }
            }


        }
    }
}