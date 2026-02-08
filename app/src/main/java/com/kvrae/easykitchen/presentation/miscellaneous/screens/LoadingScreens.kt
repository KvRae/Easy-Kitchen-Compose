package com.kvrae.easykitchen.presentation.miscellaneous.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.kvrae.easykitchen.R
import com.kvrae.easykitchen.presentation.miscellaneous.components.MealCoveredImageShimmer


@Composable
fun CircularLoadingScreen(
    modifier: Modifier = Modifier,
    lottieRawRes: Int = R.raw.food_loading,
    size: Dp = 360.dp
) {
    Box(
        modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center
    ) {
        LottieAnimation(
            rawRes = lottieRawRes,
            modifier = Modifier.size(size)
        )

    }
}

@Composable
fun LoadingTransparentScreen(
    modifier: Modifier = Modifier,
    lottieRawRes: Int = R.raw.food_loading,
    size: Dp = 360.dp
) {
    Dialog(
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false),
        onDismissRequest = {}
    ) {
        Box(
            modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LottieAnimation(
                rawRes = lottieRawRes,
                modifier = Modifier.size(size)
            )
        }

    }
}

@Composable
fun MealsImageCoveredListLoad(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
        ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        repeat(4) {
            MealCoveredImageShimmer()
        }
    }
}

@Composable
fun LottieAnimation(
    modifier: Modifier = Modifier,
    iterations: Int = LottieConstants.IterateForever,
    rawRes: Int = R.raw.food_loading,
) {
    val restart = iterations == 1
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(rawRes))
    LottieAnimation(
        modifier = modifier,
        composition = composition,
        iterations = iterations,
        restartOnPlay = restart,

        )
}
