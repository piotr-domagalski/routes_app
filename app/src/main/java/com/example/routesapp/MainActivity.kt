package com.example.routesapp

import android.R
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.routesapp.ui.theme.RoutesAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var clickCount = remember { mutableStateOf(0) }

            RoutesAppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        Greeting(
                            name = "Android",
                            modifier = Modifier.padding(start=16.dp, bottom=16.dp, top=32.dp)
                        )
                    },
                    bottomBar = {
                        Counter(
                            clickCount = clickCount.value,
                            onClick = { clickCount.value++ }
                        )
                    }
                ) { innerPadding ->
                    FruitList(innerPadding)
                }
            }
        }
    }
}

@Composable
fun FruitList(innerPadding: PaddingValues) {
    var fruits = listOf("Apple", "Banana", "Pear", "Dragon fruit", "Tomato", "Peach", "Strawberry", "Orange", "Lemon")
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        verticalArrangement = Arrangement.spacedBy(8.dp), // odstępy między elementami
        contentPadding = PaddingValues(vertical = 8.dp)    // margines wewnętrzny listy
    ) {
        // Funkcja items iteruje po liście 'fruits' i dla każdego elementu wywołuje blok lambda
        items(fruits) { fruit ->
            Text(
                text = fruit,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
@Composable
@Preview(showBackground = true)
fun Counter(clickCount: Int, onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text(text = "Clicks: $clickCount")
    }
}
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RoutesAppTheme {
        Greeting("Android")
    }
}