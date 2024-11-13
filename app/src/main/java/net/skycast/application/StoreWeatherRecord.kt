package net.skycast.application

import net.skycast.domain.Location
import net.skycast.domain.WeatherInfo

interface StoreWeatherRecord : UseCase<Pair<Location, WeatherInfo>, Long>

