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
 * @param ts Unix Timestamp
 * @param timestampLocal Timestamp in local time
 * @param timestampUtc Timestamp UTC
 * @param datetime Date in format \"YYYY-MM-DD:HH\". All datetime is in (UTC)
 * @param snow Accumulated snowfall since last forecast point - default (mm)
 * @param precip Accumulated precipitation since last forecast point - default (mm)
 * @param temp Temperature (Average) - default (C)
 * @param dewpt Dewpoint (Average) - default (C)
 * @param maxTemp Maximum daily Temperature - default (C)
 * @param minTemp Minimum daily Temperature - default (C)
 * @param appMaxTemp Apparent Maximum daily Temperature - default (C)
 * @param appMinTemp Apparent Minimum daily Temperature - default (C)
 * @param rh Relative Humidity as a percentage (%)
 * @param clouds Cloud cover as a percentage (%)
 * @param weather 
 * @param slp Mean Sea level pressure (mb)
 * @param pres Pressure (mb)
 * @param uv UV Index
 * @param vis Average Visibility default (KM)
 * @param pop Chance of Precipitation as a percentage (%)
 * @param moonPhase Moon phase
 * @param sunriseTs Sunrise unix timestamp
 * @param sunsetTs Sunset unix timestamp
 * @param moonriseTs Moonrise unix timestamp
 * @param moonsetTs Moonset unix timestamp
 * @param pod Part of the day (d = day, n = night)
 * @param windSpd Wind Speed (default m/s)
 * @param windDir Wind direction
 * @param windCdir Cardinal wind direction
 * @param windCdirFull Cardinal wind direction (text)
 */
@Serializable
data class Forecast (

    /* Unix Timestamp */
    @SerialName("ts")
    val ts: Double? = null,

    /* Timestamp in local time */
    @SerialName("timestamp_local")
    val timestampLocal: String? = null,

    /* Timestamp UTC */
    @SerialName("timestamp_utc")
    val timestampUtc: String? = null,

    /* Date in format \"YYYY-MM-DD:HH\". All datetime is in (UTC) */
    @SerialName("datetime")
    val datetime: String? = null,

    /* Accumulated snowfall since last forecast point - default (mm) */
    @SerialName("snow")
    val snow: Double? = null,

    /* Accumulated precipitation since last forecast point - default (mm) */
    @SerialName("precip")
    val precip: Double? = null,

    /* Temperature (Average) - default (C) */
    @SerialName("temp")
    val temp: Double? = null,

    /* Dewpoint (Average) - default (C) */
    @SerialName("dewpt")
    val dewpt: Double? = null,

    /* Maximum daily Temperature - default (C) */
    @SerialName("max_temp")
    val maxTemp: Double? = null,

    /* Minimum daily Temperature - default (C) */
    @SerialName("min_temp")
    val minTemp: Double? = null,

    /* Apparent Maximum daily Temperature - default (C) */
    @SerialName("app_max_temp")
    val appMaxTemp: Double? = null,

    /* Apparent Minimum daily Temperature - default (C) */
    @SerialName("app_min_temp")
    val appMinTemp: Double? = null,

    /* Relative Humidity as a percentage (%) */
    @SerialName("rh")
    val rh: Int? = null,

    /* Cloud cover as a percentage (%) */
    @SerialName("clouds")
    val clouds: Int? = null,

    @SerialName("weather")
    val weather: WeatherConditions? = null,

    /* Mean Sea level pressure (mb) */
    @SerialName("slp")
    val slp: Double? = null,

    /* Pressure (mb) */
    @SerialName("pres")
    val pres: Double? = null,

    /* UV Index */
    @SerialName("uv")
    val uv: Double? = null,

    /* Average Visibility default (KM) */
    @SerialName("vis")
    val vis: Double? = null,

    /* Chance of Precipitation as a percentage (%) */
    @SerialName("pop")
    val pop: Double? = null,

    /* Moon phase */
    @SerialName("moon_phase")
    val moonPhase: Double? = null,

    /* Sunrise unix timestamp */
    @SerialName("sunrise_ts")
    val sunriseTs: Int? = null,

    /* Sunset unix timestamp */
    @SerialName("sunset_ts")
    val sunsetTs: Int? = null,

    /* Moonrise unix timestamp */
    @SerialName("moonrise_ts")
    val moonriseTs: Int? = null,

    /* Moonset unix timestamp */
    @SerialName("moonset_ts")
    val moonsetTs: Int? = null,

    /* Part of the day (d = day, n = night) */
    @SerialName("pod")
    val pod: String? = null,

    /* Wind Speed (default m/s) */
    @SerialName("wind_spd")
    val windSpd: Double? = null,

    /* Wind direction */
    @SerialName("wind_dir")
    val windDir: Int? = null,

    /* Cardinal wind direction */
    @SerialName("wind_cdir")
    val windCdir: String? = null,

    /* Cardinal wind direction (text) */
    @SerialName("wind_cdir_full")
    val windCdirFull: String? = null

)