package com.example.castanedamariana.presentation.assetList

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.CurrencyBitcoin
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.castanedamariana.presentation.AppRoutes
import com.example.castanedamariana.presentation.components.ErrorLayout
import com.example.castanedamariana.presentation.components.LoadingLayout
import com.example.castanedamariana.presentation.model.AssetUi
import com.example.castanedamariana.ui.theme.CryptoAppTheme
import kotlin.collections.isNotEmpty

fun NavGraphBuilder.assetListRoute(
    onAssetClick: (String) -> Unit
) {
    composable<AppRoutes.AssetList> {
        val viewModel: AssetListViewModel = viewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()
        
        AssetListScreen(
            state = state,
            onAssetClick = onAssetClick,
            onEvent = viewModel::onEvent,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AssetListScreen(
    state: AssetListScreenState,
    onAssetClick: (String) -> Unit,
    onEvent: (AssetListScreenEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("CryptoMarket")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            if (!state.isLoading && !state.isError && state.data.isNotEmpty()) {
                FloatingActionButton(
                    onClick = { onEvent(AssetListScreenEvent.SaveOfflineClick) },
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                ) {
                    if (state.isSavingOffline) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(Icons.Default.CloudDownload, contentDescription = "Guardar offline")
                    }
                }
            }
        }
    ) { paddingValues ->
        when {
            state.isLoading -> LoadingLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
            state.isError -> ErrorLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                onRetryClick = { onEvent(AssetListScreenEvent.RetryClick) }
            )
            else -> AssetListSuccessState(
                assets = state.data,
                dataSource = state.dataSource,
                lastUpdateDateTime = state.lastUpdateDateTime,
                onAssetClick = onAssetClick,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
        }
    }
}

@Composable
private fun AssetListSuccessState(
    assets: List<AssetUi>,
    dataSource: DataSource,
    lastUpdateDateTime: String,
    onAssetClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Indicador de fuente de datos
        if (dataSource != DataSource.UNKNOWN) {
            DataSourceIndicator(
                dataSource = dataSource,
                lastUpdateDateTime = lastUpdateDateTime,
                modifier = Modifier.fillMaxWidth()
            )
        }
        
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(assets) { asset ->
                AssetItem(
                    asset = asset,
                    onClick = { onAssetClick(asset.id) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun DataSourceIndicator(
    dataSource: DataSource,
    lastUpdateDateTime: String,
    modifier: Modifier = Modifier
) {
    val text = when (dataSource) {
        DataSource.ONLINE -> "Viendo data más reciente"
        DataSource.OFFLINE -> if (lastUpdateDateTime.isNotEmpty()) {
            "Viendo data del $lastUpdateDateTime"
        } else {
            ""
        }
        DataSource.UNKNOWN -> ""
    }
    
    if (text.isNotEmpty()) {
        Box(
            modifier = modifier
                .background(
                    if (dataSource == DataSource.ONLINE) 
                        MaterialTheme.colorScheme.primaryContainer
                    else 
                        MaterialTheme.colorScheme.secondaryContainer
                )
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium,
                color = if (dataSource == DataSource.ONLINE)
                    MaterialTheme.colorScheme.onPrimaryContainer
                else
                    MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

@Composable
private fun AssetItem(
    asset: AssetUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Ícono de cripto
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = asset.symbol.take(2),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Info principal
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = asset.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = asset.symbol,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Precio y cambio
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = asset.priceFormatted,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Icon(
                        imageVector = if (asset.isPositiveChange) 
                            Icons.Default.ArrowDropUp 
                        else 
                            Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        tint = if (asset.isPositiveChange)
                            MaterialTheme.colorScheme.tertiary
                        else
                            MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = asset.changePercentFormatted,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (asset.isPositiveChange)
                            MaterialTheme.colorScheme.tertiary
                        else
                            MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewAssetListScreen() {
    CryptoAppTheme {
        Surface {
            AssetListScreen(
                state = AssetListScreenState(
                    isLoading = false,
                    data = listOf(
                        AssetUi(
                            id = "bitcoin",
                            rank = "1",
                            symbol = "BTC",
                            name = "Bitcoin",
                            supply = "19000000",
                            maxSupply = "21000000",
                            marketCapUsd = "800000000000",
                            volumeUsd24Hr = "20000000000",
                            priceUsd = "42000.50",
                            changePercent24Hr = "2.5",
                            vwap24Hr = "41500.00"
                        ),
                        AssetUi(
                            id = "ethereum",
                            rank = "2",
                            symbol = "ETH",
                            name = "Ethereum",
                            supply = "120000000",
                            maxSupply = null,
                            marketCapUsd = "250000000000",
                            volumeUsd24Hr = "10000000000",
                            priceUsd = "2200.75",
                            changePercent24Hr = "-1.2",
                            vwap24Hr = "2180.00"
                        )
                    ),
                    dataSource = DataSource.ONLINE
                ),
                onAssetClick = {},
                onEvent = {},
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
