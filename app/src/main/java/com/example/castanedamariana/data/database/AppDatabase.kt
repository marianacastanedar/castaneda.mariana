package com.example.castanedamariana.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.castaneda.mariana.data.database.entities.AssetEntity
import com.example.castanedamariana.data.database.dao.AssetDao

@Database(
    entities = [AssetEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun assetDao(): AssetDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "crypto_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
