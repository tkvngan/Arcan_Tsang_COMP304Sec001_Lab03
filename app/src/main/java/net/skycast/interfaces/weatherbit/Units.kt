package net.skycast.interfaces.weatherbit

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * enum for parameter units
 */
@Serializable
enum class Units(val value: String) {
    @SerialName("M")
    Metric("M"),
    @SerialName("S")
    Scientific("S"),
    @SerialName("I")
    Fahrenheit("I");

    override fun toString(): String = value
}