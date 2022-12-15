package de.minimaximal.networktoolkit.api.ripe.addressspaceusage

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ModelDataAllocation(
    val allocation: String,
    val asn_name: String,
    val assignments: Int,
    val status: String
)