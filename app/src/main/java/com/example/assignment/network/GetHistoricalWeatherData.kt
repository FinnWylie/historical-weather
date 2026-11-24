package com.example.assignment.network

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL


suspend fun fetchProducts(): List<Map<String, Any?>> = withContext(Dispatchers.IO) {
    var connection: HttpURLConnection? = null
    try {
        connection = (URL("https://dummyjson.com/products").openConnection() as
                HttpURLConnection).apply {
            requestMethod = "GET"
        }

        if (connection.responseCode == HttpURLConnection.HTTP_OK) {
            val response
                    = connection.inputStream.bufferedReader().use { it.readText() }
            val json = JSONObject(response)
            val products = json.getJSONArray("products")

            // Convert each JSONObject in the array into a Map<String, Any?>
            val resultList = mutableListOf<Map<String, Any?>>()
            for (i in 0 until products.length()) {
                val productObject = products.getJSONObject(i)
                val productMap = mutableMapOf<String, Any?>()

                val keys = productObject.keys()
                while (keys.hasNext()) {
                    val key = keys.next()
                    val raw = productObject.opt(key) // get value for that key
                    val mapped: Any? = when (raw) {
                        JSONObject.NULL -> null

                        is JSONObject, is JSONArray -> raw.toString()
                        else -> raw
                    }
                    productMap[key] = mapped
                }

                resultList.add(productMap)
            }

            resultList
        } else {
            emptyList()
        }
    } catch (e: Exception) {
        Log.e("GetProducts", "Error fetching products", e)
        emptyList()
    } finally {
        connection?.disconnect()
    }
}