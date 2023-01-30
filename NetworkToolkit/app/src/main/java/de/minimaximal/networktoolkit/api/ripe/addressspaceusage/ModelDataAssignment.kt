package de.minimaximal.networktoolkit.api.ripe.addressspaceusage

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ModelDataAssignment(
    val address_range: String,
    val asn_name: String,
    val parent_allocation: String,
    val status: String
)