package de.minimaximal.networktoolkit

import android.content.ContextWrapper
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
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
        val arrScreens = arrayOf("main", "ipstack", "ping", "publicip")
        var textStyle = TextStyle(
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.secondary,
            textAlign = TextAlign.Left,
            fontSize = 20.sp
        )
        Scaffold(
            scaffoldState = scaffoldState,
            drawerShape = NavShape(0.dp, 0.5f),
            drawerContent = {
                arrScreens.forEach { element ->
                    if (element != "main") textStyle = TextStyle(
                        textAlign = TextAlign.Left,
                        fontSize = 20.sp

                    )
                    TextButton(onClick = {
                        currentScreen = element
                        scope.launch {
                            scaffoldState.drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    }, modifier = Modifier.padding(horizontal = 16.dp)) {
                        Text(getTitle(element), style = textStyle)
                    }
                    Divider()

                }

            },

            topBar = {
                TopAppBar {
                    Button(onClick = {
                        scope.launch {
                            scaffoldState.drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    }) {
                        Text(text = "Network Toolkit")
                    }
                    Text(getTitle(currentScreen), modifier = Modifier.padding(16.dp))
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
                }

            }
        }
    }
}

private fun getTitle(screen: String): String {
    var title = "OVERVIEW"
    when (screen) {
        "main" -> title = "OVERVIEW"
        "ipstack" -> title = "IP STACK"
        "ping" -> title = "PING"
        "publicip" -> title = "PUBLIC IP"
    }
    return title
}

class NavShape(
    private val widthOffset: Dp,
    private val scale: Float
) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Rectangle(
            Rect(
                Offset.Zero,
                Offset(
                    size.width * scale + with(density) { widthOffset.toPx() },
                    size.height
                )
            )
        )
    }
}
