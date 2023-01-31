package de.minimaximal.networktoolkit

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Json(name = "audit")
data class Audit (
    val createdDate: String,
    val updatedDate: String
)