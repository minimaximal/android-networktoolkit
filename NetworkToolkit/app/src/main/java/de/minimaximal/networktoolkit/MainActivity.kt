// TODO: make side bar text field full width or make sidebar smaller
// TODO: show current View in top bar
// TODO: change color sheme, purple an dark mode looks awfully
// TODO: sidebar: highlight the main "Network Toolkit"
// TODO: sidebar: Text is not vertically aligned

package de.minimaximal.networktoolkit

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.net.ConnectivityManager
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
                    val contextWrapper = ContextWrapper(this)
                    HomeScreen(contextWrapper)
                }
            }
        }
    }
}

@Composable
fun HomeScreen(contextWrapper: ContextWrapper) {
    NetworkToolkitTheme {
        var currentScreen by remember { mutableStateOf("main") }
        val scaffoldState = rememberScaffoldState()
        val scope = rememberCoroutineScope()
        val arrScreens = arrayOf("main", "ipstack","ping","publicip","whois")
        val arrText = arrayOf("Network Toolkit", "IP Stack","Ping","Public Ip","WhoIs")
        Scaffold(
            scaffoldState = scaffoldState,
            drawerContent = {
                arrScreens.forEachIndexed() { index, element ->
                    TextButton(onClick = {
                        currentScreen = element
                        scope.launch {
                            scaffoldState.drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    }, modifier = Modifier.padding(horizontal = 16.dp)) {
                        Text(arrText[index])
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
                }
            }
        )
        { contentPadding ->
            Box(modifier = Modifier.padding(contentPadding)) {
                when (currentScreen) {
                    "main" -> MainScreen()
                    "ipstack" -> IpStackView(contextWrapper)
                    "ping" -> PingHostView()
                    "publicip" -> PublicIpView()
                    "whois" -> Text("aktuelle Ansicht: $currentScreen")
                }

            }
        }
    }
}
