package com.kvrae.easykitchen.presentation.forget_password

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kvrae.easykitchen.presentation.miscellaneous.components.FormButton
import com.kvrae.easykitchen.presentation.miscellaneous.components.PasswordInput
import com.kvrae.easykitchen.presentation.miscellaneous.components.TextInput


import com.kvrae.easykitchen.utils.EMAIL_FPS_ROUTE
import com.kvrae.easykitchen.utils.FORGET_PASS_SCREEN_ROUTE
import com.kvrae.easykitchen.utils.LOGIN_SCREEN_ROUTE
import com.kvrae.easykitchen.utils.OTP_FPS_ROUTE
import com.kvrae.easykitchen.utils.PASSWORD_FPS_ROUTE
import com.kvrae.easykitchen.utils.popThenNavigateTo

@Composable
fun ForgetPasswordScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val navRoute = remember {
        mutableStateOf(EMAIL_FPS_ROUTE)
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        when (navRoute.value) {
            PASSWORD_FPS_ROUTE -> PasswordFPScreen(
                onConfirm = {
                    navController.popThenNavigateTo(
                        navigateRoute = LOGIN_SCREEN_ROUTE,
                        popRoute = FORGET_PASS_SCREEN_ROUTE
                    )
                }
            )

            OTP_FPS_ROUTE -> OtpFPScreen(
                onConfirm = {
                    navRoute.value = PASSWORD_FPS_ROUTE
                }

            )

            else -> EmailFPScreen(
                modifier = modifier,
                onConfirm = {
                    navRoute.value = OTP_FPS_ROUTE
                }
            )

        }
    }

    BackHandler {
        when (navRoute.value) {
            PASSWORD_FPS_ROUTE -> navRoute.value = OTP_FPS_ROUTE
            OTP_FPS_ROUTE -> navRoute.value = EMAIL_FPS_ROUTE
            else -> navController.popThenNavigateTo(
                navigateRoute = LOGIN_SCREEN_ROUTE,
                popRoute = FORGET_PASS_SCREEN_ROUTE
            )

        }
    }

}

@Composable
fun EmailFPScreen(
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (emailValue, setEmailValue) = remember { mutableStateOf("") }
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isVisible = true
    }

    val contentAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(600, delayMillis = 100),
        label = "Content alpha"
    )

    val contentOffset by animateFloatAsState(
        targetValue = if (isVisible) 0f else 30f,
        animationSpec = tween(600, easing = FastOutSlowInEasing),
        label = "Content offset"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo with animation
        Box(
            modifier = Modifier
                .size(100.dp)
                .padding(bottom = 32.dp)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(24.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Email,
                contentDescription = "Logo",
                modifier = Modifier.size(60.dp),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        // Main Title
        Text(
            text = "Recover Your Account",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp
            ),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .alpha(contentAlpha)
        )

        // Subtitle
        Text(
            text = "Enter your email address and we'll send you a verification code to reset your password",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 14.sp,
                lineHeight = 20.sp
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(bottom = 32.dp)
                .alpha(contentAlpha)
        )

        // Email Input with animation
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = contentOffset.dp)
                .alpha(contentAlpha)
        ) {
            TextInput(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                placeholder = "name@example.com",
                label = "Email Address",
                value = emailValue,
                onValueChange = setEmailValue,
                leadingIcon = Icons.Rounded.Email,
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Send Button
        FormButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .alpha(contentAlpha),
            text = "Send Recovery Code",
            onClick = onConfirm,
            enabled = emailValue.isNotBlank()
        )

        // Helper Text
        Text(
            text = "We'll never share your email with anyone else",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 16.dp)
                .alpha(contentAlpha)
        )
    }
}

@Composable
fun OtpFPScreen(
    modifier: Modifier = Modifier,
    onConfirm: () -> Unit
) {
    val (otpValue, setOtpValue) = remember { mutableStateOf("") }
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isVisible = true
    }

    val contentAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(600, delayMillis = 100),
        label = "Content alpha"
    )

    val contentOffset by animateFloatAsState(
        targetValue = if (isVisible) 0f else 30f,
        animationSpec = tween(600, easing = FastOutSlowInEasing),
        label = "Content offset"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo with animation
        Box(
            modifier = Modifier
                .size(100.dp)
                .padding(bottom = 32.dp)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(24.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Lock,
                contentDescription = "Logo",
                modifier = Modifier.size(60.dp),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        // Main Title
        Text(
            text = "Verify Your Email",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp
            ),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .alpha(contentAlpha)
        )

        // Subtitle
        Text(
            text = "We've sent a verification code to your email address. Please enter it below to continue.",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 14.sp,
                lineHeight = 20.sp
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(bottom = 32.dp)
                .alpha(contentAlpha)
        )

        // OTP Input with animation
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = contentOffset.dp)
                .alpha(contentAlpha)
        ) {
            TextInput(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                placeholder = "000000",
                label = "Verification Code",
                value = otpValue,
                onValueChange = setOtpValue,
                leadingIcon = Icons.Rounded.CheckCircle,
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Verify Button
        FormButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .alpha(contentAlpha),
            text = "Verify Code",
            onClick = onConfirm,
            enabled = otpValue.isNotBlank()
        )

        // Helper Text
        Text(
            text = "Didn't receive the code? Check your spam folder or request a new one.",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 16.dp)
                .alpha(contentAlpha)
        )
    }
}

@Composable
fun PasswordFPScreen(
    modifier: Modifier = Modifier,
    onConfirm: () -> Unit
) {
    val (passwordValue, setPasswordValue) = remember { mutableStateOf("") }
    val (confirmPasswordValue, setConfirmPasswordValue) = remember { mutableStateOf("") }
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isVisible = true
    }

    val contentAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(600, delayMillis = 100),
        label = "Content alpha"
    )

    val contentOffset by animateFloatAsState(
        targetValue = if (isVisible) 0f else 30f,
        animationSpec = tween(600, easing = FastOutSlowInEasing),
        label = "Content offset"
    )

    val passwordsMatch = passwordValue.isNotBlank() && passwordValue == confirmPasswordValue
    val isButtonEnabled =
        passwordValue.isNotBlank() && confirmPasswordValue.isNotBlank() && passwordsMatch

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo with animation
        Box(
            modifier = Modifier
                .size(100.dp)
                .padding(bottom = 32.dp)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(24.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Lock,
                contentDescription = "Logo",
                modifier = Modifier.size(60.dp),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        // Main Title
        Text(
            text = "Create New Password",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp
            ),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .alpha(contentAlpha)
        )

        // Subtitle
        Text(
            text = "Enter a strong password to secure your account. Make sure it's at least 8 characters long.",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 14.sp,
                lineHeight = 20.sp
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(bottom = 32.dp)
                .alpha(contentAlpha)
        )

        // Password Input with animation
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = contentOffset.dp)
                .alpha(contentAlpha),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PasswordInput(
                modifier = Modifier.fillMaxWidth(),
                placeholder = "Enter your password",
                label = "New Password",
                value = passwordValue,
                onValueChange = setPasswordValue,
                leadingIcon = Icons.Rounded.Lock,
            )

            PasswordInput(
                modifier = Modifier.fillMaxWidth(),
                placeholder = "Confirm your password",
                label = "Confirm Password",
                value = confirmPasswordValue,
                onValueChange = setConfirmPasswordValue,
                leadingIcon = Icons.Rounded.Lock,
                isError = confirmPasswordValue.isNotBlank() && !passwordsMatch,
                errorText = if (confirmPasswordValue.isNotBlank() && !passwordsMatch) "Passwords do not match" else ""
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Confirm Button
        FormButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .alpha(contentAlpha),
            text = "Reset Password",
            onClick = onConfirm,
            enabled = isButtonEnabled
        )

        // Helper Text
        Text(
            text = "Your password must be strong and unique",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 16.dp)
                .alpha(contentAlpha)
        )
    }
}
