package de.minimaximal.networktoolkit

import de.minimaximal.networktoolkit.api.ripe.whatsmyip.Model
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.minimaximal.networktoolkit.ui.theme.NetworkToolkitTheme
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        StrictMode.setThreadPolicy(ThreadPolicy.Builder().permitAll().build())

        setContent {
            NetworkToolkitTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    App()

                }
            }
        }
    }


    @Composable
    fun App() {

        var ipAddress by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = {
                ipAddress = getIpAddress()
            }) {
                Text("Get IP Address")
            }

            Text(
                modifier = Modifier.clickable {
                    // Call the API to get the IP address
                    ipAddress = getIpAddress()

                },
                text = "ip:$ipAddress"
            )
        }
    }


    interface WhatsMyIpApi {
        @GET("data/whats-my-ip/data.json")
        fun getIpAddress(): Call<Model>
    }





    private val retrofit: Retrofit? = Retrofit.Builder()
        .baseUrl("https://stat.ripe.net/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    private val api = retrofit?.create(WhatsMyIpApi::class.java)

    private fun getIpAddress(): String {
        val response = api?.getIpAddress()?.execute()
        if (response != null) {
            return response.body()?.data?.ip ?: "data"
        }
        return ""
    }
}