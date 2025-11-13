package com.example.castanedamariana.presentation.assetList

sealed interface AssetListScreenEvent {
    data object SaveOfflineClick : AssetListScreenEvent
    data object RetryClick : AssetListScreenEvent
}
