package com.example.castanedamariana.presentation.assetList

import com.example.castanedamariana.presentation.model.AssetUi

data class AssetListScreenState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val data: List<AssetUi> = listOf(),
    val dataSource: DataSource = DataSource.UNKNOWN,
    val lastUpdateDateTime: String = "",
    val isSavingOffline: Boolean = false
)

enum class DataSource {
    ONLINE,
    OFFLINE,
    UNKNOWN
}
