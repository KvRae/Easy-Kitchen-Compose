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
import androidx.compose.material.icons.rounded.Email
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
fun EmailFPScreen(
    modifier: Modifier = Modifier,
    viewModel: ForgetPasswordViewModel,
    onSuccess: () -> Unit,
    onError: (String) -> Unit,
) {
    var emailValue by remember { mutableStateOf("") }
    var isVisible by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf<String?>(null) }

    val emailState by viewModel.emailInputState.collectAsState()

    LaunchedEffect(Unit) {
        isVisible = true
    }

    LaunchedEffect(emailState) {
        when (emailState) {
            is EmailInputState.Success -> {
                onSuccess()
            }

            is EmailInputState.Error -> {
                onError((emailState as EmailInputState.Error).message)
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

    val isLoading = emailState is EmailInputState.Loading
    val isEmailValid = ForgotPasswordValidator.isValidEmail(emailValue)

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
                imageVector = Icons.Rounded.Email,
                contentDescription = "Email recovery icon",
                modifier = Modifier.size(60.dp),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

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
                onValueChange = { newValue ->
                    emailValue = newValue
                    emailError = ForgotPasswordValidator.getEmailError(newValue)
                },
                leadingIcon = Icons.Rounded.Email,
                enabled = !isLoading,
                isError = emailError != null,
                errorText = emailError ?: ""
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        FormButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .alpha(contentAlpha),
            text = if (isLoading) "Sending..." else "Send Recovery Code",
            onClick = {
                if (isEmailValid) {
                    viewModel.requestPasswordReset(ForgotPasswordValidator.sanitizeEmail(emailValue))
                }
            },
            enabled = isEmailValid && !isLoading
        )

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
