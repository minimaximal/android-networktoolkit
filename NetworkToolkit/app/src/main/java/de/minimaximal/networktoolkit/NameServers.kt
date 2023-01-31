package de.minimaximal.networktoolkit

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Json(name = "nameServers")
data class NameServers (
    val rawText: String,
    val hostNames: List<String>,
    val ips: List<Any?>
)