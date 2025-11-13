package com.example.castanedamariana.presentation.assetDetail

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.navigation.toRoute
import com.example.castanedamariana.presentation.assetList.DataSource
import com.example.castanedamariana.presentation.components.ErrorLayout
import com.example.castanedamariana.presentation.components.LoadingLayout
import com.example.castanedamariana.presentation.model.AssetUi
import com.example.castanedamariana.ui.theme.CryptoAppTheme
import com.example.castanedamariana.presentation.AppRoutes

fun NavGraphBuilder.assetDetailRoute(
    onNavigateBack: () -> Unit
) {
    composable<AppRoutes.AssetDetail> { backStackEntry ->
        val route: AppRoutes.AssetDetail = backStackEntry.toRoute()
        val viewModel: AssetDetailViewModel = viewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()
        
        LaunchedEffect(Unit) {
            viewModel.fetchData(route.assetId)
        }
        
        AssetDetailScreen(
            state = state,
            onNavigateBack = onNavigateBack,
            onRetryClick = { viewModel.fetchData(route.assetId) },
            modifier = Modifier.fillMaxSize()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AssetDetailScreen(
    state: AssetDetailScreenState,
    onNavigateBack: () -> Unit,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(state.data?.name ?: "Criptomoneda")
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
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
                onRetryClick = onRetryClick
            )
            state.data != null -> AssetDetailSuccessState(
                asset = state.data,
                dataSource = state.dataSource,
                lastUpdateDateTime = state.lastUpdateDateTime,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
        }
    }
}

@Composable
private fun AssetDetailSuccessState(
    asset: AssetUi,
    dataSource: DataSource,
    lastUpdateDateTime: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        if (dataSource != DataSource.UNKNOWN) {
            DataSourceIndicator(
                dataSource = dataSource,
                lastUpdateDateTime = lastUpdateDateTime,
                modifier = Modifier.fillMaxWidth()
            )
        }
        
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = asset.symbol.take(3),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = asset.name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            
            Text(
                text = asset.symbol,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
            )
        }
        
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Precio actual",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = asset.priceFormatted,
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
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
                        modifier = Modifier.size(32.dp)
                    )
                    Text(
                        text = asset.changePercentFormatted,
                        style = MaterialTheme.typography.titleLarge,
                        color = if (asset.isPositiveChange)
                            MaterialTheme.colorScheme.tertiary
                        else
                            MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Text(
                    text = "Últimas 24h",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
        
        Text(
            text = "Información del mercado",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        
        InfoCard(
            title = "Supply",
            value = asset.supplyFormatted,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp)
        )
        
        InfoCard(
            title = "Max Supply",
            value = asset.maxSupplyFormatted,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp)
        )
        
        InfoCard(
            title = "Market Cap",
            value = asset.marketCapFormatted,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp)
        )
        
        InfoCard(
            title = "Rank",
            value = "#${asset.rank}",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
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
private fun InfoCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewAssetDetailScreen() {
    CryptoAppTheme {
        Surface {
            AssetDetailScreen(
                state = AssetDetailScreenState(
                    isLoading = false,
                    data = AssetUi(
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
                    dataSource = DataSource.ONLINE
                ),
                onNavigateBack = {},
                onRetryClick = {},
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
