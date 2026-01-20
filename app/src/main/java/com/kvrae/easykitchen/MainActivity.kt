package com.kvrae.easykitchen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.kvrae.easykitchen.ui.theme.EasyKitchenTheme
import com.kvrae.easykitchen.utils.App

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EasyKitchenTheme {
                App()
            }
        }
    }
}