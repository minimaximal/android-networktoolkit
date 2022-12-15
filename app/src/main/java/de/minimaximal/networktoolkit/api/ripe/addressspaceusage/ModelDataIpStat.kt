package de.minimaximal.networktoolkit.api.ripe.addressspaceusage

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ModelDataIpStat(
    val ips: Int,
    val status: String
)