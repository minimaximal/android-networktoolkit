package de.minimaximal.networktoolkit

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import de.minimaximal.networktoolkit.ui.theme.NetworkToolkitTheme


class MainActivity : ComponentActivity() {

    var ipAddress = ""


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContent {
            NetworkToolkitTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    button()

                }
            }
        }
    }

    @Composable
    fun button() {
        // Use a Column to lay out the UI elements vertically
        Column {
            // Create a button to trigger the API query


            Button(onClick = { getIpAddress() }) {
                Text("Get IP Address")
            }


        }
    }
    @Composable
    private fun getIpAddress() {
        val url = "https://stat.ripe.net/data/whats-my-ip/data.json"
        val request = Request.Builder().url(url).build()
        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                TODO("Not yet implemented")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body()?.string()
                val json = JSONObject(responseData)
                ipAddress = json.getString("data")
            }
        }
        )
        Text(ipAddress)
    }
}
}
