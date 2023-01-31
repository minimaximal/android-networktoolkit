package de.minimaximal.networktoolkit

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Json(name = "administrativeContact")
data class AdministrativeContact (
    val organization: String,
    val state: String,
    val country: String,
    val countryCode: String,
    val rawText: String
)