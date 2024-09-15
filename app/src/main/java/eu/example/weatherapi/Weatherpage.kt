package eu.example.weatherapi

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import eu.example.weatherapi.api.NetworkResponse
import eu.example.weatherapi.api.WeatherModel
import java.lang.reflect.Modifier

@Composable
fun WeatherPage(viewModel: WeatherViewModel) {
    var city by remember {
        mutableStateOf("")
    }
    val weatherResult =viewModel.weatherResult.observeAsState()

    Column(
        modifier = androidx.compose.ui.Modifier.fillMaxWidth().padding(8.dp),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally

    ) {
        Row (
            modifier = androidx.compose.ui.Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ){
            OutlinedTextField(modifier = androidx.compose.ui.Modifier.weight(1f),
                value = city,
                onValueChange = { city = it },
                label = {
                    Text(text = "Serach for any location")
                }
            )
            IconButton(onClick = {viewModel.getData(city)})
            {
                Icon(imageVector = Icons.Default.Search,
                    contentDescription = "Search for any location" )
            }
        }
        when(val result =weatherResult.value){
            is NetworkResponse.Error -> {
                Text(text = result.message)
            }
            NetworkResponse.Loading -> {
                CircularProgressIndicator()
            }
            is NetworkResponse.Success ->{
                WeatherDetails(data = result.data )
            }
            null->{
            }
        }

    }
}

@Composable
fun WeatherDetails(data: WeatherModel){
    Column (
        modifier = androidx.compose.ui.Modifier.fillMaxWidth().padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Row (
            modifier = androidx.compose.ui.Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Bottom
        ){
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Location Icon", //location
                modifier = androidx.compose.ui.Modifier.size(40.dp)
            )
            Text(text = data.location.name, fontSize = 30.sp)
            Spacer(modifier = androidx.compose.ui.Modifier.width(8.dp))
            Text(text = data.location.country, fontSize = 18.sp,color = Color.Gray)
        }
        Spacer(modifier = androidx.compose.ui.Modifier.height(16.dp))
        Text(
            text = "${data.current.temp_c}°c" ,
            fontSize = 56.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        AsyncImage(
            modifier = androidx.compose.ui.Modifier.size(160.dp),
            model = "https:${data.current.condition.icon}".replace("64x64","128x128"),
            contentDescription = "Condition Icon"
        )
            Text(
                text = data.current.condition.text ?: "Unknown Condition",
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
        Spacer(modifier = androidx.compose.ui.Modifier.height(16.dp))
        Card {
            Column(
                modifier = androidx.compose.ui.Modifier.fillMaxWidth()
            ) {
                Row(
                modifier = androidx.compose.ui.Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                WeatherKeyVal("Humidity:",data.current.humidity)
                WeatherKeyVal("Wind Speed:",data.current.wind_kph+"km/h ")

            }
                Row(
                    modifier = androidx.compose.ui.Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    WeatherKeyVal("UV:",data.current.uv)
                    WeatherKeyVal("Participation:",data.current.precip_mm)

                }
                Row(
                    modifier = androidx.compose.ui.Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    WeatherKeyVal("Local Time:",data.location.localtime.split( " ")[1])
                    WeatherKeyVal("Local Data:",data.location.localtime.split( " ")[0])

                }

            }
        }





    }

}
@Composable
fun WeatherKeyVal(key : String, value : String){
    Column(
        modifier = androidx.compose.ui.Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text=value, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = key, fontWeight = FontWeight.SemiBold, color = Color.Gray)
    }
}