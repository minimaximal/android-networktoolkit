package de.minimaximal.networktoolkit.api.ripe.addressspaceusage
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Model(
    val build_version: String,
    val cached: Boolean,
    val `data`: ModelData,
    val data_call_name: String,
    val data_call_status: String,
    val messages: List<List<String>>,
    val process_time: Int,
    val query_id: String,
    val see_also: List<Any>,
    val server_id: String,
    val status: String,
    val status_code: Int,
    val time: String,
    val version: String
)