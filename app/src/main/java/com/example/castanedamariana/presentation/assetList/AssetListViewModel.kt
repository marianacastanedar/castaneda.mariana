package com.example.castanedamariana.presentation.assetList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.castanedamariana.data.repository.AssetRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AssetListViewModel(application: Application) : AndroidViewModel(application) {
    private val _state = MutableStateFlow(AssetListScreenState())
    val state = _state.asStateFlow()
    
    private val repository = AssetRepository(application)

    init {
        fetchData()
    }

    fun onEvent(event: AssetListScreenEvent) {
        when (event) {
            AssetListScreenEvent.SaveOfflineClick -> saveOffline()
            AssetListScreenEvent.RetryClick -> fetchData()
        }
    }

    private fun fetchData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, isError = false) }
            
            val result = repository.getAssets()
            val lastUpdate = repository.getLastUpdateDateTime()
            val hasOfflineData = repository.hasOfflineData()

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
                            data = assetResult.assets,
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

    private fun saveOffline() {
        viewModelScope.launch {
            _state.update { it.copy(isSavingOffline = true) }
            
            val result = repository.saveAssetsOffline()
            
            result.fold(
                onSuccess = {
                    val lastUpdate = repository.getLastUpdateDateTime()
                    _state.update {
                        it.copy(
                            isSavingOffline = false,
                            lastUpdateDateTime = lastUpdate,
                            dataSource = DataSource.ONLINE
                        )
                    }
                },
                onFailure = {
                    _state.update {
                        it.copy(isSavingOffline = false)
                    }
                }
            )
        }
    }
}
