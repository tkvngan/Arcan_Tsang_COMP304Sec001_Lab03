package net.skycast

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import net.skycast.application.GetWeather
import net.skycast.infrastructure.UseCaseImplementations
import net.skycast.infrastructure.room.AppRepository
import net.skycast.infrastructure.weatherbit.WeatherbitApi
import net.skycast.ui.theme.SkyCastTheme

class MainActivity : ComponentActivity() {

    val repository by lazy { AppRepository(context = this) }
    val weatherApi by lazy { WeatherbitApi(key = "d62ef40519fc42989301bd120efc75e0") }
    val useCases by lazy { UseCaseImplementations(repository, weatherApi) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SkyCastTheme {
                Scaffold(modifier = Modifier.fillMaxSize().padding(8.dp)) { innerPadding ->
                    val scope = rememberCoroutineScope()
                    Simple(
                        modifier = Modifier.padding(innerPadding),
                        useCases = useCases,
                        repository = repository,
                        scope = scope
                    )
                }
            }
        }
    }
}

@Composable
fun Simple(
    modifier: Modifier = Modifier,
    repository: AppRepository,
    useCases: UseCaseImplementations,
    scope: CoroutineScope
) {
    var text = remember { mutableStateOf("") }
    Column(modifier = modifier.fillMaxWidth()) {
        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(16.dp),
            onClick = {
                scope.launch {
                    useCases.GetWeather(
                        GetWeather.Parameters(city="Toronto", country="CA")
                    )?.let { (location, weatherInfo) ->
                        text.value = location.toString() + "\n" + weatherInfo.toString()
                        useCases.StoreWeatherRecord(Pair(location, weatherInfo))
                    }
                }
            },
        ) {
            Text("Current Weather")
        }
        Text(
            text = text.value,
        )
    }

}
