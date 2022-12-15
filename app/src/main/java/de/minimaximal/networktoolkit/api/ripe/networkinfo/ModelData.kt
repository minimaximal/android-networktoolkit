package de.minimaximal.networktoolkit.api.ripe.networkinfo

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ModelData(
    val asns: List<String>,
    val prefix: String
)