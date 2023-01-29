// TODO: instant / faster ui update of result list
// TODO: scrollbar in result list: vertical, optional horizontal
// TODO: statistics are not displayed if there is any not working

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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.*
import java.net.InetAddress

@Composable
fun PingHostView() {
    val message = remember { mutableStateOf("") }
    val result = remember { mutableListOf<PingHostResult>() }
    val statistics = remember { mutableStateOf(PingHostStatistics(null,null,null)) }
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
            message.value = ""
            result.clear()
            statistics
            try {
                val timeoutInt = timeout.value.toInt();
                val numberInt = numberOfPings.value.toInt();
                val delayLong = delay.value.toLong();
                pingHost(result, host.value, timeoutInt, numberInt, delayLong, statistics)
            } catch (e: Exception) {
                message.value = "ERROR: please check your input parameters"
            }

        }) {
            Text("Ping it")
        }

        if (message.value != "") {
            Text(text = message.value)
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 24.dp)
            )
            {
                items(result.size) { index ->
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(30.dp)) {
                        item {
                            when (result[index].state) {
                                -1 -> Text(text = (index + 1).toString() + " " + "I/O error, please check network or input")
                                0 -> Text(text = (index + 1).toString() + " " + host.value + " not reachable")
                                1 -> Text(text = (index + 1).toString() + " " + host.value + " is reachable")
                                else -> {
                                    Text(text = "general error")
                                }
                            }
                        }
                        item {
                            Text(text = result[index].latency.toString() + " ms")
                        }
                    }
                }
            }
            if (statistics.value.successfulPings != null && statistics.value.successPercentage != null && statistics.value.averageLatency != null ) {
                Text(text = statistics.value.successfulPings.toString() + " successful Pings of " + numberOfPings.value + " total Pings (" + statistics.value.successPercentage.toString() + "% success rate)")
                Text(text = statistics.value.averageLatency.toString() + "ms average latency")
            }
        }
    }
}






fun pingHost(result: MutableList<PingHostResult>, host: String, timeout: Int, numberOfPings: Int, delay: Long, statistics: MutableState<PingHostStatistics>) {
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

fun pingHostStatistics(result: MutableList<PingHostResult>, numberOfPings: Int, statistics: MutableState<PingHostStatistics>){

    var successfulPings = 0
    var totalLatency = 0

    result.forEach {
        if (it.state == 1) {
            successfulPings++
            totalLatency += it.latency.toInt()
        }
    }

    statistics.value.successfulPings = successfulPings
    statistics.value.successPercentage = (successfulPings / numberOfPings) * 100
    statistics.value.averageLatency = totalLatency / successfulPings
}



data class PingHostResult (val state: Int, val latency: Long)
data class PingHostStatistics(var successfulPings: Int?, var successPercentage: Int?, var averageLatency: Int?)