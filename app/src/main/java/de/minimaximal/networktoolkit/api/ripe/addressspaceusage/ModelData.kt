package de.minimaximal.networktoolkit.api.ripe.addressspaceusage

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ModelData(
    val allocations: List<ModelDataAllocation>,
    val assignments: List<ModelDataAssignment>,
    val ip_stats: List<ModelDataIpStat>,
    val query_time: String,
    val resource: String
)