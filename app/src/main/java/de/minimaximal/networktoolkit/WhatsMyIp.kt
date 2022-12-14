package de.minimaximal.networktoolkit

data class WhatsMyIp(
    val build_version: String,
    val cached: Boolean,
    val `data`: Data,
    val data_call_name: String,
    val data_call_status: String,
    val messages: List<Any>,
    val process_time: Int,
    val query_id: String,
    val see_also: List<Any>,
    val server_id: String,
    val status: String,
    val status_code: Int,
    val time: String,
    val version: String
)