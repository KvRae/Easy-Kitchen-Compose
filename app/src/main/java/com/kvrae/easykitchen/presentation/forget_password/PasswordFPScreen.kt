package com.kvrae.easykitchen.presentation.forget_password

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import com.kvrae.easykitchen.presentation.miscellaneous.components.PasswordInput

@Composable
fun PasswordFPScreen(
    modifier: Modifier = Modifier,
    viewModel: ForgetPasswordViewModel,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    var passwordValue by remember { mutableStateOf("") }
    var confirmPasswordValue by remember { mutableStateOf("") }
    var isVisible by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }

    val resetState by viewModel.passwordResetState.collectAsState()

    LaunchedEffect(Unit) {
        isVisible = true
    }

    LaunchedEffect(resetState) {
        when (resetState) {
            is PasswordResetState.Success -> {
                onSuccess()
            }

            is PasswordResetState.Error -> {
                onError((resetState as PasswordResetState.Error).message)
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

    val isPasswordValid = passwordValue.length >= 8
    val passwordsMatch = passwordValue.isNotBlank() && passwordValue == confirmPasswordValue
    val isButtonEnabled = isPasswordValid && confirmPasswordValue.isNotBlank() && passwordsMatch
    val isLoading = resetState is PasswordResetState.Loading

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
                contentDescription = "Password reset icon",
                modifier = Modifier.size(60.dp),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

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
                onValueChange = { newValue ->
                    passwordValue = newValue
                    passwordError = if (newValue.isNotBlank() && newValue.length < 8) {
                        "Password must be at least 8 characters"
                    } else {
                        null
                    }
                },
                leadingIcon = Icons.Rounded.Lock,
                isError = passwordError != null,
                errorText = passwordError ?: "",
                enabled = !isLoading
            )

            PasswordInput(
                modifier = Modifier.fillMaxWidth(),
                placeholder = "Confirm your password",
                label = "Confirm Password",
                value = confirmPasswordValue,
                onValueChange = { newValue ->
                    confirmPasswordValue = newValue
                    confirmPasswordError = if (newValue.isNotBlank() && newValue != passwordValue) {
                        "Passwords do not match"
                    } else {
                        null
                    }
                },
                leadingIcon = Icons.Rounded.Lock,
                isError = confirmPasswordError != null,
                errorText = confirmPasswordError ?: "",
                enabled = !isLoading
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        FormButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .alpha(contentAlpha),
            text = if (isLoading) "Resetting..." else "Reset Password",
            onClick = {
                if (isButtonEnabled) {
                    viewModel.resetPassword(passwordValue)
                }
            },
            enabled = isButtonEnabled && !isLoading
        )

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
