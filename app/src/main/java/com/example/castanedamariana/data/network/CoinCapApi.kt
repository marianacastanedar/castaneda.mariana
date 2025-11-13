package com.example.castanedamariana.data.network

import com.example.castanedamariana.data.network.dto.AssetDto
import com.example.castanedamariana.data.network.dto.AssetsResponseDto
import com.example.castanedamariana.data.network.dto.SingleAssetResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class CoinCapApi {
    private val httpClient = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }

        install(Logging) {
            logger = Logger.ANDROID
            level = LogLevel.ALL
        }

        defaultRequest {
            url("https://api.coincap.io/v3/")
            header("Authorization", "Bearer 6f8c2f757cc81e9950a05aeed8292abff853114ebc731977f3f5a580b1e9371a")
        }
    }

    suspend fun getAssets(): AssetsResponseDto {
        return httpClient.get("assets").body()
    }

    suspend fun getAssetById(id: String): AssetDto {
        val response: SingleAssetResponseDto = httpClient.get("assets/$id").body()
        return response.data
    }
}
