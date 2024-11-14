package net.skycast.ui

import net.skycast.R
import net.skycast.domain.PartOfDay

fun getWeatherIcon(weatherCode: String, partOfDay: PartOfDay? = null): Int {
    val (day, night) = when (weatherCode) {
        "200" -> Pair(R.drawable.t01d, R.drawable.t01n)
        "201" -> Pair(R.drawable.t02d, R.drawable.t02n)
        "202" -> Pair(R.drawable.t03d, R.drawable.t03n)
        "230" -> Pair(R.drawable.t04d, R.drawable.t04n)
        "231" -> Pair(R.drawable.t04d, R.drawable.t04n)
        "232" -> Pair(R.drawable.t04d, R.drawable.t04n)
        "233" -> Pair(R.drawable.t05d, R.drawable.t05n)
        "300" -> Pair(R.drawable.d01d, R.drawable.d01n)
        "301" -> Pair(R.drawable.d02d, R.drawable.d02n)
        "302" -> Pair(R.drawable.d03d, R.drawable.d03n)
        "500" -> Pair(R.drawable.r01d, R.drawable.r01n)
        "501" -> Pair(R.drawable.r02d, R.drawable.r02n)
        "502" -> Pair(R.drawable.r03d, R.drawable.r03n)
        "511" -> Pair(R.drawable.f01d, R.drawable.f01n)
        "520" -> Pair(R.drawable.r04d, R.drawable.r04n)
        "521" -> Pair(R.drawable.r05d, R.drawable.r05n)
        "522" -> Pair(R.drawable.r06d, R.drawable.r06n)
        "600" -> Pair(R.drawable.s01d, R.drawable.s01n)
        "601" -> Pair(R.drawable.s02d, R.drawable.s02n)
        "602" -> Pair(R.drawable.s03d, R.drawable.s03n)
        "610" -> Pair(R.drawable.s04d, R.drawable.s04n)
        "611" -> Pair(R.drawable.s05d, R.drawable.s05n)
        "612" -> Pair(R.drawable.s05d, R.drawable.s05n)
        "621" -> Pair(R.drawable.s01d, R.drawable.s01n)
        "622" -> Pair(R.drawable.s02d, R.drawable.s02n)
        "623" -> Pair(R.drawable.s06d, R.drawable.s06n)
        "700" -> Pair(R.drawable.a01d, R.drawable.a01n)
        "711" -> Pair(R.drawable.a02d, R.drawable.a02n)
        "721" -> Pair(R.drawable.a03d, R.drawable.a03n)
        "731" -> Pair(R.drawable.a04d, R.drawable.a04n)
        "741" -> Pair(R.drawable.a05d, R.drawable.a05n)
        "751" -> Pair(R.drawable.a06d, R.drawable.a06n)
        "800" -> Pair(R.drawable.c01d, R.drawable.c01n)
        "801" -> Pair(R.drawable.c02d, R.drawable.c02n)
        "802" -> Pair(R.drawable.c02d, R.drawable.c02n)
        "803" -> Pair(R.drawable.c03d, R.drawable.c03n)
        "804" -> Pair(R.drawable.c04d, R.drawable.c04n)
        "900" -> Pair(R.drawable.u00d, R.drawable.u00n)
        else -> throw IllegalArgumentException("Unknown weather code: $weatherCode")
    }
    return when (partOfDay) {
        PartOfDay.DAY -> day
        PartOfDay.NIGHT -> night
        null -> day
    }
}