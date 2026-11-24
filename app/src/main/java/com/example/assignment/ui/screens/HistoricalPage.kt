package com.example.assignment.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.assignment.R
import com.example.assignment.network.fetchProducts



@Composable
fun HistoricalWeatherScreen(modifier: Modifier = Modifier
) {
    ProductInfoDisplay()
}



@Composable
fun ProductInfoDisplay() {
    // Nullable means "still loading"
    var products by remember { mutableStateOf<List<Map<String, Any?>>?>(null) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            products = fetchProducts() // Fetch list of product maps
        } catch (e: Exception) {
            error = e.message
            products = emptyList()
        }
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White)
        ) {
            when {
                error != null -> {
                    Text(
                        text = "Error: $error",
                        color = Color.Red,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                products == null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                products!!.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No products found")
                    }
                }

                else -> {
                    Text(
                        text = "Product List",
                        fontSize = 22.sp,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(12.dp)
                    )

                    // Display each product in a LazyColumn
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(12.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(products!!.size) { index ->
                            val item = products!![index]

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFF3F3F3))
                                    .padding(12.dp)
                            ) {
                                Column {
                                    Text(
                                        text = item["title"]?.toString() ?:
                                        "(no title)",
                                        fontSize = 18.sp,
                                        color = Color.Black
                                    )
                                    Text(
                                        text = item["description"]?.toString() ?:
                                        "(no description)",
                                        fontSize = 14.sp,
                                        color = Color.DarkGray
                                    )
                                    Text(
                                        text = "Price: ${item["price"] ?: "?"} USD",
                                        fontSize = 14.sp,
                                        color = Color.Blue
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading)
    )
}


@Composable
fun ErrorScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text(text = stringResource(R.string.loading_failed), modifier = Modifier.padding(16.dp))
    }
}


@Composable
fun ResultScreen(data: String, modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(data)
    }
}