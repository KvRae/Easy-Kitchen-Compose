package com.kvrae.easykitchen.presentation.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kvrae.easykitchen.R
import com.kvrae.easykitchen.presentation.miscellaneous.components.FormButton
import com.kvrae.easykitchen.presentation.onboarding.components.OnboardingStepIndicator
import com.kvrae.easykitchen.utils.LOGIN_SCREEN_ROUTE
import com.kvrae.easykitchen.utils.ONBOARDING_SCREEN_ROUTE
import com.kvrae.easykitchen.utils.popThenNavigateTo

@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    var currentStep by remember { mutableIntStateOf(0) }
    val totalSteps = 3

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .systemBarsPadding()
    ) {
        // Animated content for smooth transitions
        AnimatedContent(
            targetState = currentStep,
            transitionSpec = {
                slideInHorizontally(
                    initialOffsetX = { if (targetState > initialState) it else -it }
                ).togetherWith(
                    slideOutHorizontally(
                        targetOffsetX = { if (targetState > initialState) -it else it }
                    )
                )
            },
            modifier = Modifier.fillMaxSize(),
            label = "onboarding_transition"
        ) { step ->
            when (step) {
                0 -> OnboardingStep1()
                1 -> OnboardingStep2()
                2 -> OnboardingStep3()
                else -> OnboardingStep1()
            }
        }

        // Navigation Controls
        OnboardingNavigationBar(
            currentStep = currentStep,
            totalSteps = totalSteps,
            onBackClick = {
                if (currentStep > 0) {
                    currentStep--
                }
            },
            onNextClick = {
                if (currentStep < totalSteps - 1) {
                    currentStep++
                }
            },
            onSkipClick = {
                navController.popThenNavigateTo(
                    navigateRoute = LOGIN_SCREEN_ROUTE,
                    popRoute = ONBOARDING_SCREEN_ROUTE
                )
            },
            onGetStartedClick = {
                navController.popThenNavigateTo(
                    navigateRoute = LOGIN_SCREEN_ROUTE,
                    popRoute = ONBOARDING_SCREEN_ROUTE
                )
            }
        )
    }
}

@Composable
private fun BoxScope.OnboardingNavigationBar(
    currentStep: Int,
    totalSteps: Int,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    onSkipClick: () -> Unit,
    onGetStartedClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .align(Alignment.BottomCenter),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Step Indicators
        OnboardingStepIndicator(
            currentStep = currentStep,
            totalSteps = totalSteps,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // Action Buttons
        if (currentStep == totalSteps - 1) {
            // Last step - Show Get Started button
            FormButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = onGetStartedClick,
                text = "Get Started"
            )
        } else {
            // Other steps - Show navigation buttons
            androidx.compose.foundation.layout.Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back button
                if (currentStep > 0) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                } else {
                    Box(modifier = Modifier.padding(16.dp))
                }

                // Skip button
                androidx.compose.material3.TextButton(onClick = onSkipClick) {
                    androidx.compose.material3.Text(
                        text = "Skip",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Next button
                IconButton(onClick = onNextClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                        contentDescription = "Next",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}


@Composable
private fun OnboardingStep1(modifier: Modifier = Modifier) {
    OnboardingStepContent(
        title = "Welcome to Easy Kitchen",
        description = "Discover delicious recipes and unleash your culinary creativity with our comprehensive meal collection",
        lottieRawRes = R.raw.food_choosen,
        modifier = modifier
    )
}

@Composable
private fun OnboardingStep2(modifier: Modifier = Modifier) {
    OnboardingStepContent(
        title = "Smart Cooking Guide",
        description = "Let our AI chef assistant guide you step-by-step through every recipe with detailed instructions and helpful tips",
        lottieRawRes = R.raw.cooking,
        modifier = modifier
    )
}

@Composable
private fun OnboardingStep3(modifier: Modifier = Modifier) {
    OnboardingStepContent(
        title = "Create with What You Have",
        description = "Select ingredients from your pantry and discover amazing recipes you can cook right now",
        lottieRawRes = R.raw.ingredients,
        modifier = modifier
    )
}

@Composable
private fun OnboardingStepContent(
    title: String,
    description: String,
    lottieRawRes: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 140.dp), // Leave space for navigation bar
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
    ) {
        // Lottie Animation
        com.airbnb.lottie.compose.LottieAnimation(
            composition = com.airbnb.lottie.compose.rememberLottieComposition(
                com.airbnb.lottie.compose.LottieCompositionSpec.RawRes(lottieRawRes)
            ).value,
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(280.dp),
            iterations = com.airbnb.lottie.compose.LottieConstants.IterateForever
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Title
        androidx.compose.material3.Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Description
        androidx.compose.material3.Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}
