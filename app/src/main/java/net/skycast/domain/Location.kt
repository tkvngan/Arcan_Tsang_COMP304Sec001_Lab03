package net.skycast.domain

data class Location(
    val latitude: Double? = null,
    val longitude: Double? = null,
    val city: String? = null,
    val stateCode: String? = null,
    val countryCode: String? = null,
    val postalCode: String? = null
)
