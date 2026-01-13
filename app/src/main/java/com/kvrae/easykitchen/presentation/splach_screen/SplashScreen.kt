package com.kvrae.easykitchen.presentation.splach_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.kvrae.easykitchen.R
import com.kvrae.easykitchen.presentation.login.LoginViewModel
import com.kvrae.easykitchen.utils.LOGIN_SCREEN_ROUTE
import com.kvrae.easykitchen.utils.MAIN_SCREEN_ROUTE
import com.kvrae.easykitchen.utils.SPLASH_SCREEN_ROUTE
import com.kvrae.easykitchen.utils.popThenNavigateTo
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun SplashScreen(navController: NavController) {
    val loginViewModel = koinViewModel<LoginViewModel>()
    val isLoggedIn = loginViewModel.isLoggedIn.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(key1 = Unit) {
        coroutineScope.launch {
            delay(3000)
            if (isLoggedIn.value) {
                navController.popThenNavigateTo(
                    navigateRoute = MAIN_SCREEN_ROUTE,
                    popRoute = SPLASH_SCREEN_ROUTE
                )
                return@launch
            }
            navController.popThenNavigateTo(
                navigateRoute = LOGIN_SCREEN_ROUTE,
                popRoute = SPLASH_SCREEN_ROUTE
            )
        }
    }
    LottieSplashScreen()
}

@Composable
fun LottieSplashScreen() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lottie_animation))
    val progress by animateLottieCompositionAsState(composition)
    LottieAnimation(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxSize(),
        composition = composition,
        progress = progress,
    )
}