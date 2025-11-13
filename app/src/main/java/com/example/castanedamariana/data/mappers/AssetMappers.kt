package com.example.castanedamariana.data.mappers

import com.example.castaneda.mariana.data.database.entities.AssetEntity
import com.example.castanedamariana.data.network.dto.AssetDto
import com.example.castanedamariana.presentation.model.AssetUi

fun AssetDto.toEntity(): AssetEntity {
    return AssetEntity(
        id = this.id,
        rank = this.rank,
        symbol = this.symbol,
        name = this.name,
        supply = this.supply,
        maxSupply = this.maxSupply,
        marketCapUsd = this.marketCapUsd,
        volumeUsd24Hr = this.volumeUsd24Hr,
        priceUsd = this.priceUsd,
        changePercent24Hr = this.changePercent24Hr,
        vwap24Hr = this.vwap24Hr
    )
}

// Entity -> UI
fun AssetEntity.toUi(): AssetUi {
    return AssetUi(
        id = this.id,
        rank = this.rank,
        symbol = this.symbol,
        name = this.name,
        supply = this.supply,
        maxSupply = this.maxSupply,
        marketCapUsd = this.marketCapUsd,
        volumeUsd24Hr = this.volumeUsd24Hr,
        priceUsd = this.priceUsd,
        changePercent24Hr = this.changePercent24Hr,
        vwap24Hr = this.vwap24Hr
    )
}

// DTO -> UI (directo cuando viene de internet)
fun AssetDto.toUi(): AssetUi {
    return AssetUi(
        id = this.id,
        rank = this.rank,
        symbol = this.symbol,
        name = this.name,
        supply = this.supply,
        maxSupply = this.maxSupply,
        marketCapUsd = this.marketCapUsd,
        volumeUsd24Hr = this.volumeUsd24Hr,
        priceUsd = this.priceUsd,
        changePercent24Hr = this.changePercent24Hr,
        vwap24Hr = this.vwap24Hr
    )
}
