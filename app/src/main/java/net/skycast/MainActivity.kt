package net.skycast

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import net.skycast.interfaces.weatherbit.WeatherbitApi
import net.skycast.ui.theme.SkyCastTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SkyCastTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val api = WeatherbitApi(key = "d62ef40519fc42989301bd120efc75e0")
                    val scope = rememberCoroutineScope()
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding),
                        api = api,
                        scope = scope
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier, api: WeatherbitApi, scope: CoroutineScope) {
    var text = remember { mutableStateOf("Hello") }

    Column(modifier = modifier) {
        Button(
            onClick = {
                scope.launch {
                    val observations = api.getCurrentObservations(lat = 43.7, lon = -79.42)
                    text.value = observations.toString()

                }
            },
        ) {
            Text("Click me")
        }
        Text(
            text = text.value,
        )
    }

}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    SkyCastTheme {
//        Greeting("Android")
//    }
//}