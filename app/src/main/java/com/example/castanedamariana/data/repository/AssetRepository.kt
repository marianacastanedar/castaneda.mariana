package com.example.castanedamariana.data.repository

import android.app.Application
import com.example.castanedamariana.data.database.AppDatabase
import com.example.castanedamariana.data.mappers.toEntity
import com.example.castanedamariana.data.mappers.toUi
import com.example.castanedamariana.data.network.CoinCapApi
import com.example.castanedamariana.data.preferences.AppPreferences
import com.example.castanedamariana.data.preferences.dataStore
import com.example.castanedamariana.data.repository.AssetResult
import com.example.castanedamariana.data.repository.SingleAssetResult

class AssetRepository(
    application: Application
) {
    private val database = AppDatabase.getDatabase(application)
    private val api = CoinCapApi()
    private val preferences = AppPreferences(application.dataStore)

    suspend fun getAssets(): Result<AssetResult> {
        return try {
            val response = api.getAssets()
            val assets = response.data.map { it.toUi() }
            Result.success(AssetResult(assets = assets, isFromCache = false))
        } catch (e: Exception) {
            try {
                val localAssets = database.assetDao().getAllAssets()
                if (localAssets.isNotEmpty()) {
                    val assets = localAssets.map { it.toUi() }
                    Result.success(AssetResult(assets = assets, isFromCache = true))
                } else {
                    Result.failure(e)
                }
            } catch (localException: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun getAssetById(id: String): Result<SingleAssetResult> {
        return try {
            val asset = api.getAssetById(id)
            Result.success(SingleAssetResult(asset = asset.toUi(), isFromCache = false))
        } catch (e: Exception) {
            try {
                val localAsset = database.assetDao().getAssetById(id)
                if (localAsset != null) {
                    Result.success(SingleAssetResult(asset = localAsset.toUi(), isFromCache = true))
                } else {
                    Result.failure(e)
                }
            } catch (localException: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun saveAssetsOffline(): Result<Unit> {
        return try {
            val response = api.getAssets()
            val entities = response.data.map { it.toEntity() }
            
            database.assetDao().deleteAllAssets()
            database.assetDao().insertAssets(entities)
            
            preferences.updateLastUpdateDateTime()
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getLastUpdateDateTime(): String {
        return preferences.getLastUpdateDateTime()
    }

    suspend fun hasOfflineData(): Boolean {
        return try {
            database.assetDao().getAllAssets().isNotEmpty()
        } catch (e: Exception) {
            false
        }
    }
}
