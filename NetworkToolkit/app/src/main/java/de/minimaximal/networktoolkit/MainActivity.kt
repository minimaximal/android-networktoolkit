package de.minimaximal.networktoolkit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import de.minimaximal.networktoolkit.ui.theme.NetworkToolkitTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NetworkToolkitTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    HomeScreen()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreen() {
    NetworkToolkitTheme() {
        var currentScreen by remember { mutableStateOf("main") }
        Scaffold(
            topBar = {
                TopAppBar() {
                    Button(onClick = { currentScreen = "main" }) {
                        Text("Network Toolkit")
                    }
                    Button(onClick = { currentScreen = "ping" }) {
                        Text("Ping")
                    }
                    Button(onClick = { currentScreen = "publicip" }) {
                        Text("Public IP")
                    }
                    Button(onClick = { currentScreen = "whois" }) {
                        Text("WhoIs")
                    }
                }
            }
        )
        { contentPadding ->
            Box(modifier = Modifier.padding(contentPadding)) {
                when (currentScreen) {
                    "main" -> MainScreen()
                    "ping" -> PingHostView()
                    "publicip" -> PublicIpView()
                    "whois" -> Text("aktuelle Ansicht: $currentScreen")
                }

            }
        }
    }
}