package com.example.castanedamariana.presentation.model

data class AssetUi(
    val id: String,
    val rank: String,
    val symbol: String,
    val name: String,
    val supply: String,
    val maxSupply: String?,
    val marketCapUsd: String,
    val volumeUsd24Hr: String,
    val priceUsd: String,
    val changePercent24Hr: String,
    val vwap24Hr: String?
) {
    val changePercentFormatted: String
        get() = try {
            val value = changePercent24Hr.toDouble()
            String.format("%.2f%%", value)
        } catch (e: Exception) {
            "0.00%"
        }

    val isPositiveChange: Boolean
        get() = try {
            changePercent24Hr.toDouble() >= 0
        } catch (e: Exception) {
            false
        }

    val priceFormatted: String
        get() = try {
            val value = priceUsd.toDouble()
            String.format("$%.2f", value)
        } catch (e: Exception) {
            "$0.00"
        }

    val marketCapFormatted: String
        get() = try {
            val value = marketCapUsd.toDouble()
            when {
                value >= 1_000_000_000 -> String.format("$%.2fB", value / 1_000_000_000)
                value >= 1_000_000 -> String.format("$%.2fM", value / 1_000_000)
                else -> String.format("$%.2f", value)
            }
        } catch (e: Exception) {
            "$0.00"
        }

    val supplyFormatted: String
        get() = try {
            val value = supply.toDouble()
            when {
                value >= 1_000_000_000 -> String.format("%.2fB", value / 1_000_000_000)
                value >= 1_000_000 -> String.format("%.2fM", value / 1_000_000)
                else -> String.format("%.2f", value)
            }
        } catch (e: Exception) {
            "0.00"
        }

    val maxSupplyFormatted: String
        get() = maxSupply?.let {
            try {
                val value = it.toDouble()
                when {
                    value >= 1_000_000_000 -> String.format("%.2fB", value / 1_000_000_000)
                    value >= 1_000_000 -> String.format("%.2fM", value / 1_000_000)
                    else -> String.format("%.2f", value)
                }
            } catch (e: Exception) {
                "N/A"
            }
        } ?: "N/A"
}
