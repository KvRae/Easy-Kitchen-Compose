package com.kvrae.easykitchen.presentation.splash

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
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
    val splashViewModel = koinViewModel<SplashViewModel>()
    val isLoggedIn = loginViewModel.isLoggedIn.collectAsState()
    val serverPingState = splashViewModel.serverPingState.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(key1 = Unit) {
        coroutineScope.launch {
            // Wait for splash duration and server ping to complete
            delay(3000)

            // Navigate regardless of ping state (server may be waking up)
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
    GradientSplashScreen()
}

@Composable
fun GradientSplashScreen() {
    var startAnimation by remember { mutableStateOf(false) }

    // Animate logo scale
    val scale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.3f,
        animationSpec = tween(
            durationMillis = 1000,
            easing = FastOutSlowInEasing
        ),
        label = "logo_scale"
    )

    // Animate logo alpha
    val alpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 1000,
            easing = FastOutSlowInEasing
        ),
        label = "logo_alpha"
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
    }

    val primaryColor = MaterialTheme.colorScheme.primary
    val tertiaryColor = MaterialTheme.colorScheme.tertiary

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        primaryColor,
                        tertiaryColor
                    ),
                    startY = 0f,
                    endY = Float.POSITIVE_INFINITY
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_light),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(200.dp)
                .scale(scale)
                .alpha(alpha)
        )
    }
}