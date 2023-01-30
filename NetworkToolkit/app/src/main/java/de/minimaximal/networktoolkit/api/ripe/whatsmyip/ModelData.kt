package de.minimaximal.networktoolkit.api.ripe.whatsmyip

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ModelData(
    val ip: String
)