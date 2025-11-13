package com.example.castanedamariana.presentation.assetDetail

import com.example.castanedamariana.presentation.assetList.DataSource
import com.example.castanedamariana.presentation.model.AssetUi

data class AssetDetailScreenState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val data: AssetUi? = null,
    val dataSource: DataSource = DataSource.UNKNOWN,
    val lastUpdateDateTime: String = ""
)
