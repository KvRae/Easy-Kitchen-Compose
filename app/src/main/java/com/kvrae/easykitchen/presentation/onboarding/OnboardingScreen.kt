package com.kvrae.easykitchen.presentation.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    var currentStep by rememberSaveable { mutableIntStateOf(0) }
    val totalSteps = 3

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .systemBarsPadding()
    ) {
        // Animated gradient overlay for visual depth
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                            Color.Transparent
                        ),
                        startY = 0f,
                        endY = 600f
                    )
                )
        )

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
            .padding(24.dp)
            .align(Alignment.BottomCenter),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Step Indicators with padding
        OnboardingStepIndicator(
            currentStep = currentStep,
            totalSteps = totalSteps,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        )

        // Action Buttons
        if (currentStep == totalSteps - 1) {
            // Last step - Show Get Started button with gradient effect
            FormButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                onClick = onGetStartedClick,
                text = "Get Started"
            )
        } else {
            // Other steps - Show enhanced navigation buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back button with hover effect
                if (currentStep > 0) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                            )
                            .size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                } else {
                    Box(modifier = Modifier.size(48.dp))
                }

                // Skip button with improved styling
                TextButton(
                    onClick = onSkipClick,
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = "Skip",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Next button with hover effect
                IconButton(
                    onClick = onNextClick,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                        )
                        .size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                        contentDescription = "Next",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
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
        subtitle = "Your Personal Culinary Companion",
        description = "Discover delicious recipes and unleash your culinary creativity with our comprehensive meal collection",
        lottieRawRes = R.raw.food_choosen,
        modifier = modifier
    )
}

@Composable
private fun OnboardingStep2(modifier: Modifier = Modifier) {
    OnboardingStepContent(
        title = "Smart Cooking Guide",
        subtitle = "AI Chef at Your Service",
        description = "Let our AI chef assistant guide you step-by-step through every recipe with detailed instructions and helpful tips",
        lottieRawRes = R.raw.cooking,
        modifier = modifier
    )
}

@Composable
private fun OnboardingStep3(modifier: Modifier = Modifier) {
    OnboardingStepContent(
        title = "Create with What You Have",
        subtitle = "Smart Ingredient Discovery",
        description = "Select ingredients from your pantry and discover amazing recipes you can cook right now",
        lottieRawRes = R.raw.ingredients,
        modifier = modifier
    )
}

@Composable
private fun OnboardingStepContent(
    title: String,
    subtitle: String,
    description: String,
    lottieRawRes: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 160.dp), // Leave space for navigation bar
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(
            8.dp,
            Alignment.CenterVertically
        )
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // Lottie Animation with smooth bouncy animation
        val transition = rememberInfiniteTransition(label = "lottie_bounce")
        val offsetY by transition.animateFloat(
            initialValue = 0f,
            targetValue = -12f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 2600,
                    easing = LinearOutSlowInEasing
                ),
                repeatMode = RepeatMode.Reverse
            ),
            label = "bounce_offset"
        )
        val scale by transition.animateFloat(
            initialValue = 1f,
            targetValue = 1.02f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 2600,
                    easing = LinearOutSlowInEasing
                ),
                repeatMode = RepeatMode.Reverse
            ),
            label = "bounce_scale"
        )

        com.airbnb.lottie.compose.LottieAnimation(
            composition = com.airbnb.lottie.compose.rememberLottieComposition(
                com.airbnb.lottie.compose.LottieCompositionSpec.RawRes(lottieRawRes)
            ).value,
            modifier = Modifier
                .fillMaxWidth(0.75f)
                .height(320.dp)
                .graphicsLayer {
                    translationY = offsetY
                    scaleX = scale
                    scaleY = scale
                },
            iterations = com.airbnb.lottie.compose.LottieConstants.IterateForever
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Title with enhanced typography
        Text(
            text = title,
            style = MaterialTheme.typography.displaySmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                lineHeight = 40.sp
            ),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            textAlign = TextAlign.Center
        )

        // Subtitle for visual hierarchy
        Text(
            text = subtitle,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            ),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Description with better contrast
        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge.copy(
                lineHeight = 24.sp,
                fontSize = 16.sp
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))
    }
}
