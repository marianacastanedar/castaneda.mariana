package com.example.castanedamariana

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.castanedamariana.presentation.AppNavigation
import com.example.castanedamariana.ui.theme.CryptoAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CryptoAppTheme {
                AppNavigation()
            }
        }
    }
}
