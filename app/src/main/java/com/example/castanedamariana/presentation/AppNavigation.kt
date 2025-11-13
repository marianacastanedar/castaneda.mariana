package com.example.castanedamariana.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.castanedamariana.presentation.assetDetail.assetDetailRoute
import com.example.castanedamariana.presentation.assetList.assetListRoute

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppRoutes.AssetList,
        modifier = modifier
    ) {
        assetListRoute(
            onAssetClick = { assetId ->
                navController.navigate(AppRoutes.AssetDetail(assetId))
            }
        )

        assetDetailRoute(
            onNavigateBack = {
                navController.popBackStack()
            }
        )
    }
}
