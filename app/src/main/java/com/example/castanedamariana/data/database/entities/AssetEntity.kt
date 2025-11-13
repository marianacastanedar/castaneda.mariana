package com.example.castaneda.mariana.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "assets")
data class AssetEntity(
    @PrimaryKey
    val id: String,
    val rank: String,
    val symbol: String,
    val name: String,
    val supply: String,
    val maxSupply: String?,
    val marketCapUsd: String,
    val volumeUsd24Hr: String,
    val priceUsd: String,
    val changePercent24Hr: String,
    val vwap24Hr: String?
)
