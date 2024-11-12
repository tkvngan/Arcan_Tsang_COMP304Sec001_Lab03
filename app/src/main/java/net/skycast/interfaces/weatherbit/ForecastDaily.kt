/**
 *
 * Please note:
 * This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * Do not edit this file manually.
 *
 */

@file:Suppress(
    "ArrayInDataClass",
    "EnumEntryName",
    "RemoveRedundantQualifierName",
    "UnusedImport"
)

package net.skycast.interfaces.weatherbit


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 
 *
 * @param cityName City Name
 * @param stateCode State Abbreviation
 * @param countryCode Country Abbreviation
 * @param lat Latitude
 * @param lon Longitude
 * @param timezone Local IANA time zone
 * @param `data` 
 */
@Serializable
data class ForecastDaily (

    /* City Name */
    @SerialName("city_name")
    val cityName: String? = null,

    /* State Abbreviation */
    @SerialName("state_code")
    val stateCode: String? = null,

    /* Country Abbreviation */
    @SerialName("country_code")
    val countryCode: String? = null,

    /* Latitude */
    @SerialName("lat")
    val lat: Double? = null,

    /* Longitude */
    @SerialName("lon")
    val lon: Double? = null,

    /* Local IANA time zone */
    @SerialName("timezone")
    val timezone: String? = null,

    @SerialName("data")
    val `data`: List<Forecast>? = null

)