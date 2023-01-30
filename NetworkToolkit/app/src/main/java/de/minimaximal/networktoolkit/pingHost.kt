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
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.*
import java.net.InetAddress

@Composable
fun PingHostView() {
    val message = remember { mutableStateOf("") }
    val result = remember { mutableStateListOf<PingHostResult>() }
    val statistics = remember { mutableStateOf(PingHostStatistics(null, null, null)) }
    var host by remember { mutableStateOf("hk.de") }
    var timeout by remember { mutableStateOf("5000") }
    var numberOfPings by remember { mutableStateOf("5") }
    var delay by remember { mutableStateOf("100") }


    Column(modifier = Modifier.padding(16.dp)) {
        TextField(value = host,
            onValueChange = { host = it },
            label = { Text("Enter host to ping:") })

        TextField(
            value = numberOfPings,
            onValueChange = { numberOfPings = it },
            label = { Text("Enter number of pings:") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        )

        TextField(
            value = timeout,
            onValueChange = { timeout = it },
            label = { Text("Enter timeout for a ping:") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        )

        TextField(
            value = delay,
            onValueChange = { delay = it },
            label = { Text("Enter delay between pings:") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        )




        LazyRow(horizontalArrangement = Arrangement.spacedBy(30.dp)) {
            item {
                Button(onClick = {
                    clearCounter(result, statistics, message)
                    try {
                        val timeoutInt = timeout.toInt()
                        val numberInt = numberOfPings.toInt()
                        val delayLong = delay.toLong()
                        if (timeoutInt == 0 || numberInt == 0 || delayLong == 0.toLong()) {
                            message.value = "ERROR: zero is not allowed as input"
                        }
                        pingHost(result, host, timeoutInt, numberInt, delayLong, statistics)
                    } catch (e: Exception) {
                        message.value = "ERROR: please check your input parameters"
                    }
                }) {
                    Text("PING")
                }
            }
            item {
                Button(onClick = {
                    clearCounter(result, statistics, message)
                }) {
                    Text("CLEAR")
                }
            }
        }


        if (message.value != "") {
            Text(text = message.value)
        } else {
            if (statistics.value.successfulPings != null && statistics.value.successPercentage != null && statistics.value.averageLatency != null) {
                Text(text = statistics.value.successfulPings.toString() + " successful Pings of " + numberOfPings + " total Pings (" + statistics.value.successPercentage.toString() + "% success rate)")
                Text(text = statistics.value.averageLatency.toString() + "ms average latency")
            }
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 24.dp)
            ) {
                items(result.size) { index ->
                    var output: String = when (result[index].state) {
                        -1 -> (index + 1).toString() + " " + "I/O error, please check network or input\t\t"
                        0 -> (index + 1).toString() + " " + host + " not reachable\t\t"
                        1 -> (index + 1).toString() + " " + host + " is reachable\t\t"
                        else -> {
                            "general error"
                        }
                    }
                    output += result[index].latency.toString() + " ms"
                    Text(text = output)
                }
            }
        }
    }
}


fun pingHost(
    result: MutableList<PingHostResult>,
    host: String,
    timeout: Int,
    numberOfPings: Int,
    delay: Long,
    statistics: MutableState<PingHostStatistics>
) {
    CoroutineScope(Dispatchers.Main).launch {
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
        pingHostStatistics(result, numberOfPings, statistics)
    }
}

fun pingHostStatistics(
    result: MutableList<PingHostResult>,
    numberOfPings: Int,
    statistics: MutableState<PingHostStatistics>
) {

    var successfulPings = 0
    var totalLatency = 0

    result.forEach {
        if (it.state == 1) {
            successfulPings++
            totalLatency += it.latency.toInt()
        }
    }

    statistics.value = PingHostStatistics(
        successfulPings, (successfulPings / numberOfPings) * 100, totalLatency / successfulPings
    )
}

fun clearCounter(
    result: MutableList<PingHostResult>,
    statistics: MutableState<PingHostStatistics>,
    message: MutableState<String>
) {
    message.value = ""
    result.clear()
    statistics.value = PingHostStatistics(null, null, null)
}


data class PingHostResult(val state: Int, val latency: Long)
data class PingHostStatistics(
    var successfulPings: Int?, var successPercentage: Int?, var averageLatency: Int?
)