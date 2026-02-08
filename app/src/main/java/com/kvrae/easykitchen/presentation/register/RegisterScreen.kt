package com.kvrae.easykitchen.presentation.register

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kvrae.easykitchen.R
import com.kvrae.easykitchen.presentation.miscellaneous.components.FormButton
import com.kvrae.easykitchen.presentation.miscellaneous.components.PasswordInput
import com.kvrae.easykitchen.presentation.miscellaneous.components.TextFormButton
import com.kvrae.easykitchen.presentation.miscellaneous.components.TextInput
import com.kvrae.easykitchen.presentation.miscellaneous.screens.LoadingTransparentScreen
import com.kvrae.easykitchen.utils.LOGIN_SCREEN_ROUTE
import com.kvrae.easykitchen.utils.REGISTER_SCREEN_ROUTE
import com.kvrae.easykitchen.utils.popThenNavigateTo
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterScreen(
    navController: NavController,
    context: Context = LocalContext.current
) {
    val registerViewModel = koinViewModel<RegisterViewModel>()
    val registerState by registerViewModel.registerState.collectAsState()

    RegisterScreenContent(
        viewModel = registerViewModel,
        onLoginClick = {
            navController.popThenNavigateTo(
                navigateRoute = LOGIN_SCREEN_ROUTE,
                popRoute = REGISTER_SCREEN_ROUTE
            )
        }

    )

    when(registerState) {

        is RegisterState.Idle -> return

        is RegisterState.Loading -> LoadingTransparentScreen()
        is RegisterState.Success -> {
            navController.popThenNavigateTo(
                navigateRoute = LOGIN_SCREEN_ROUTE,
                popRoute = REGISTER_SCREEN_ROUTE
            )
            Toast.makeText(context, "Registration successful", Toast.LENGTH_SHORT).show()
        }
        is RegisterState.Error -> {
            Toast.makeText(context, (registerState as RegisterState.Error).message, Toast.LENGTH_SHORT).show()
        }
    }

}

@Composable
fun RegisterScreenContent(
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel,
    onLoginClick: () -> Unit
) {
    val logo = if (!isSystemInDarkTheme()) R.drawable.logo_dark else R.drawable.logo_light
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
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        // Logo with decorative background
        Column(
            modifier = Modifier
                .size(120.dp)
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(logo),
                contentDescription = "Logo",
                modifier = Modifier.size(160.dp)
            )
        }

        // Main Title
        Text(
            text = "Join Us",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp
            ),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .alpha(contentAlpha)
        )

        // Subtitle
        Text(
            text = "Create your account to discover delicious recipes",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 14.sp,
                lineHeight = 20.sp
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .alpha(contentAlpha)
        )

        // Form Container with animation
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .offset(y = contentOffset.dp)
                .alpha(contentAlpha),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Username Input
            TextInput(
                value = viewModel.username.value,
                onValueChange = {
                    viewModel.username.value = it
                },
                placeholder = "name@example.com",
                label = "Full Name",
                leadingIcon = Icons.Rounded.AccountCircle,
            )

            // Email Input
            TextInput(
                value = viewModel.email.value,
                onValueChange = {
                    viewModel.email.value = it
                },
                placeholder = "your.email@example.com",
                label = "Email Address",
                leadingIcon = Icons.Rounded.Email,
            )

            // Password Input
            PasswordInput(
                value = viewModel.password.value,
                onValueChange = {
                    viewModel.password.value = it
                },
                placeholder = "Min. 8 characters",
                label = "Password",
                leadingIcon = Icons.Rounded.Lock,
            )

            // Confirm Password Input
            PasswordInput(
                placeholder = "Repeat your password",
                label = "Confirm Password",
                leadingIcon = Icons.Rounded.Lock,
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Sign Up Button
        FormButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 24.dp)
                .alpha(contentAlpha),
            text = "Create Account",
            onClick = {
                viewModel.register()
            }
        )

        // Terms & Conditions
        Text(
            text = "By signing up, you agree to our Terms & Conditions",
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 12.sp
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 12.dp)
                .alpha(contentAlpha)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Spacer(modifier = Modifier.weight(1f))

        // Login Link Section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp)
                .alpha(contentAlpha),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "Already have an account? ",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 14.sp
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            TextFormButton(
                text = "Sign In",
                onClick = {
                    onLoginClick()
                }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}