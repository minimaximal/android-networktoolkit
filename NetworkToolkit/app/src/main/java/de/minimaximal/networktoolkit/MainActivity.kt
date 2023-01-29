package de.minimaximal.networktoolkit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
fun HomeScreen(){
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
                   Button(onClick = { currentScreen = "whois" }) {
                       Text("WhoIs")
                   }
               }
           }
       )
          { contentPadding ->
           Box(modifier = Modifier.padding(contentPadding)) {
               when(currentScreen){
                   "main" -> MainScreen()
                   "ping" -> PingHostView()
                   "whois" -> Text("aktuelle Ansicht: $currentScreen")
               }

       }

   }
}}



@Composable
fun MainView() {
    var currentScreen by remember { mutableStateOf("main") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Network Toolkit") },
                actions = {
                    IconButton(onClick = { currentScreen = "ping" }) {
                        Icon(imageVector = Icons.Filled.Home, contentDescription = "Home")
                    }
                },
                backgroundColor = Color.White
            )
        },
        content = { padding -> 16
            when (currentScreen) {
                "main" -> MainView()
                "ping" -> Box(modifier = Modifier.padding(padding)) {
                    PingHostView()
                }
            }
        }
    )
}
