package de.minimaximal.networktoolkit


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.*
import java.net.InetAddress


@Composable
fun PingHostView() {
    var message: String = buildString {
    }
    val result = remember { mutableListOf<PingHostResult>() }
    val host = remember { mutableStateOf("hk.de") }
    val timeout = remember { mutableStateOf("5000") }
    val numberOfPings = remember { mutableStateOf("5") }
    val delay = remember { mutableStateOf("100") }


    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = host.value,
            onValueChange = { host.value = it },
            label = { Text("Enter host to ping:") }
        )

        TextField(
            value = numberOfPings.value,
            onValueChange = { numberOfPings.value = it },
            label = { Text("Enter number of pings:") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        )

        TextField(
            value = timeout.value,
            onValueChange = { timeout.value = it },
            label = { Text("Enter timeout for a ping:") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        )

        TextField(
            value = delay.value,
            onValueChange = { delay.value = it },
            label = { Text("Enter delay between pings:") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        )




        Button(onClick = {
            try {
                val timeoutInt = timeout.value.toInt();
                val numberInt = numberOfPings.value.toInt();
                val delayLong = delay.value.toLong();
                pingHost(result, host.value, timeoutInt, numberInt, delayLong)
            } catch (e: Exception) {
                message = "ERROR: please check all input parameters"
            }

        }) {
            Text("Ping it")
        }

        if (message.isNotEmpty()) {
            Text(text = message)
        } else {
            LazyColumn (verticalArrangement = Arrangement.spacedBy(4.dp)) {
                items(result.size) { index ->
                    LazyRow (horizontalArrangement = Arrangement.spacedBy(30.dp)) {
                        item {
                            Text(text = result[index].state.toString())
                        }
                        item{
                            Text(text = result[index].latency.toString() + " ms")
                        }
                    }
                }
            }
        }
    }
    // TODO: switch case for output string
    // TODO: recycler view ? - clear and instant, lazy,
}


fun pingHost(result: MutableList<PingHostResult>, host: String, timeout: Int, numberOfPings: Int, delay: Long) {
    CoroutineScope(Dispatchers.Main).launch {
        result.clear()
        var pings = 0
        while (pings < numberOfPings) {
            try {
                val address = withContext(Dispatchers.IO) { InetAddress.getByName(host) }
                val startTime = System.currentTimeMillis()
                val reachable = withContext(Dispatchers.IO) { address.isReachable(timeout) }
                val latency = System.currentTimeMillis() - startTime
                if (reachable) {
                    result.add(PingHostResult(1, latency))
                } else {
                    result.add(PingHostResult(0, -1))
                }
            } catch (e: Exception) {
                result.add(PingHostResult(-1, -1))
            }
            pings++
            delay(delay)
        }
    }
}

data class PingHostResult (val state: Int, val latency: Long)
