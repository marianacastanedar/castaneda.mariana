package com.example.castanedamariana.data.repository

import com.example.castanedamariana.presentation.model.AssetUi

data class AssetResult(
    val assets: List<AssetUi>,
    val isFromCache: Boolean
)

data class SingleAssetResult(
    val asset: AssetUi,
    val isFromCache: Boolean
)