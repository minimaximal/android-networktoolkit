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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.minimaximal.networktoolkit.ui.theme.NetworkToolkitTheme
import kotlinx.coroutines.launch


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
    NetworkToolkitTheme {
        var currentScreen by remember { mutableStateOf("main") }
        val scaffoldState = rememberScaffoldState()
        val scope = rememberCoroutineScope()
        val arrScreens = arrayOf("main","ping","publicip","whois")
        Scaffold(
            scaffoldState = scaffoldState,
            drawerContent = {
                arrScreens.forEach() { element ->
                    TextButton(onClick = {
                        currentScreen = element
                        scope.launch {
                            scaffoldState.drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    }, modifier = Modifier.padding(horizontal = 16.dp)) {
                        getTitle(element)
                    }
                    Divider()

                }


            },
            topBar = {
                TopAppBar() {
                    Button(onClick = {
                        scope.launch {
                            scaffoldState.drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    }) {
                        Text(text = "Network Toolkit")
                    }
                    getTitle(currentScreen)
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

@Composable
private fun getTitle(screen: String) {
    var title = Text(text = "Übersicht")
    when(screen){
        "main"      -> title = Text(text = "Übersicht", fontWeight = FontWeight.Bold )
        "ping"      -> title = Text(text = "Ping")
        "publicip"  -> title = Text(text = "Public Ip")
        "whois"     -> title = Text(text = "Who Is")
    }
    return title
}
