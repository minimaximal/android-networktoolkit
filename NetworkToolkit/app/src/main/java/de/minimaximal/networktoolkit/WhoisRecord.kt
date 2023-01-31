package de.minimaximal.networktoolkit


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WhoisRecord (
    val createdDate: String,
    val updatedDate: String,
    val expiresDate: String,
    val registrant: AdministrativeContact,
    val administrativeContact: AdministrativeContact,
    val technicalContact: AdministrativeContact,
    val domainName: String,
    val nameServers: NameServers,
    val status: String,
    val rawText: String,
    val parseCode: Long,
    val header: String,
    val strippedText: String,
    val footer: String,
    val audit: Audit,
    val registrarName: String,
    val registrarIANAID: String,
    val createdDateNormalized: String,
    val updatedDateNormalized: String,
    val expiresDateNormalized: String,
    val registryData: RegistryData,
    val contactEmail: String,

    @Json(name = "domainNameExt")
    val domainNameEXT: String,

    val estimatedDomainAge: Long
)


