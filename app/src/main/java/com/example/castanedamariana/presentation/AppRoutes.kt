package com.example.castanedamariana.presentation

import kotlinx.serialization.Serializable

sealed interface AppRoutes {
    @Serializable
    data object AssetList : AppRoutes

    @Serializable
    data class AssetDetail(val assetId: String) : AppRoutes
}
