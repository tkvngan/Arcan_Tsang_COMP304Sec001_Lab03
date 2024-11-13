package net.skycast.domain

data class Location(
    val latitude: Double,
    val longitude: Double,
    val cityName: String? = null,
    val stateCode: String? = null,
    val countryCode: String? = null,
    val timezone: String? = null
)
