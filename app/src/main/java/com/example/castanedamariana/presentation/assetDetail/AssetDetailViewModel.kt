package com.example.castanedamariana.presentation.assetDetail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.castanedamariana.data.repository.AssetRepository
import com.example.castanedamariana.data.repository.AssetResult
import com.example.castanedamariana.presentation.assetList.DataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AssetDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val _state = MutableStateFlow(AssetDetailScreenState())
    val state = _state.asStateFlow()
    
    private val repository = AssetRepository(application)

    fun fetchData(assetId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, isError = false) }
            
            val result = repository.getAssetById(assetId)
            val lastUpdate = repository.getLastUpdateDateTime()

            result.fold(
                onSuccess = { assetResult ->
                    val dataSource = if (assetResult.isFromCache) {
                        DataSource.OFFLINE
                    } else {
                        DataSource.ONLINE
                    }

                    _state.update {
                        it.copy(
                            isLoading = false,
                            data = assetResult.asset,
                            dataSource = dataSource,
                            lastUpdateDateTime = lastUpdate
                        )
                    }
                },
                onFailure = { error ->
                    _state.update {
                        it.copy(isLoading = false, isError = true)
                    }
                }
            )
        }
    }
}
