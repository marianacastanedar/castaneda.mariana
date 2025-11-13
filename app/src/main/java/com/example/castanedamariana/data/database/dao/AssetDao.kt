package com.example.castanedamariana.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.castaneda.mariana.data.database.entities.AssetEntity

@Dao
interface AssetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssets(assets: List<AssetEntity>)

    @Query("SELECT * FROM assets")
    suspend fun getAllAssets(): List<AssetEntity>

    @Query("SELECT * FROM assets WHERE id = :id")
    suspend fun getAssetById(id: String): AssetEntity?

    @Query("DELETE FROM assets")
    suspend fun deleteAllAssets()
}
