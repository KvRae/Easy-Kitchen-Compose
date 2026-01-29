package com.kvrae.easykitchen.presentation.forget_password

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.kvrae.easykitchen.presentation.miscellaneous.components.FormButton
import com.kvrae.easykitchen.presentation.miscellaneous.components.TextInput

@Composable
fun OtpFPScreen(
    modifier: Modifier = Modifier,
    viewModel: ForgetPasswordViewModel,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    var otpValue by remember { mutableStateOf("") }
    var isVisible by remember { mutableStateOf(false) }
    var otpError by remember { mutableStateOf<String?>(null) }

    val otpState by viewModel.otpVerificationState.collectAsState()

    LaunchedEffect(Unit) {
        isVisible = true
    }

    LaunchedEffect(otpState) {
        when (otpState) {
            is OtpVerificationState.Success -> {
                onSuccess()
            }

            is OtpVerificationState.Error -> {
                onError((otpState as OtpVerificationState.Error).message)
            }

            else -> {}
        }
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

    val isLoading = otpState is OtpVerificationState.Loading
    val isOtpValid = ForgotPasswordValidator.isValidOtp(otpValue)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
                contentDescription = "Verification icon",
                modifier = Modifier.size(60.dp),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

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
                onValueChange = { newValue ->
                    val sanitized = ForgotPasswordValidator.sanitizeOtp(newValue)
                    otpValue = sanitized
                    otpError = ForgotPasswordValidator.getOtpError(sanitized)
                },
                leadingIcon = Icons.Rounded.CheckCircle,
                enabled = !isLoading,
                isError = otpError != null,
                errorText = otpError ?: ""
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        FormButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .alpha(contentAlpha),
            text = if (isLoading) "Verifying..." else "Verify Code",
            onClick = {
                if (isOtpValid) {
                    viewModel.verifyResetCode(otpValue)
                }
            },
            enabled = isOtpValid && !isLoading
        )

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
