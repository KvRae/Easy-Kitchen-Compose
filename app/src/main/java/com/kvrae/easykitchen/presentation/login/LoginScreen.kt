package com.kvrae.easykitchen.presentation.login

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kvrae.easykitchen.R
import com.kvrae.easykitchen.presentation.meals.MealsViewModel
import com.kvrae.easykitchen.presentation.miscellaneous.components.FormButton
import com.kvrae.easykitchen.presentation.miscellaneous.components.GoogleSignInButton
import com.kvrae.easykitchen.presentation.miscellaneous.components.PasswordInput
import com.kvrae.easykitchen.presentation.miscellaneous.components.TextBoxForm
import com.kvrae.easykitchen.presentation.miscellaneous.components.TextFormButton
import com.kvrae.easykitchen.presentation.miscellaneous.components.TextInput
import com.kvrae.easykitchen.presentation.miscellaneous.screens.LoadingTransparentScreen
import com.kvrae.easykitchen.utils.FORGET_PASS_SCREEN_ROUTE
import com.kvrae.easykitchen.utils.LOGIN_SCREEN_ROUTE
import com.kvrae.easykitchen.utils.MAIN_SCREEN_ROUTE
import com.kvrae.easykitchen.utils.REGISTER_SCREEN_ROUTE
import org.koin.androidx.compose.koinViewModel


@Composable
fun LoginScreen(
    navController: NavController,
) {
    val loginViewModel = koinViewModel<LoginViewModel>()
    val loginState = loginViewModel.loginState.collectAsState().value
    val mealsViewModel = koinViewModel<MealsViewModel>()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        loginViewModel.resetLoginState()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LoginUILayout(
            navController = navController,
            loginViewModel = loginViewModel
        )

        // Snackbar host for error messages
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )

        when (loginState) {
            is LoginState.Loading -> {
                LoadingTransparentScreen()
            }

            is LoginState.Success -> {
                LaunchedEffect(loginState) {
                    mealsViewModel.fetchMeals()
                    navController.navigate(MAIN_SCREEN_ROUTE) {
                        launchSingleTop = true
                        popUpTo(LOGIN_SCREEN_ROUTE) {
                            inclusive = true
                        }
                    }
                }
            }

            is LoginState.Error -> {
                LaunchedEffect(loginState.message) {
                    snackbarHostState.showSnackbar(
                        message = loginState.message,
                        withDismissAction = true
                    )
                    loginViewModel.resetLoginState()
                }
            }

            else -> {}
        }
    }
}

@Composable
fun LoginUILayout(
    context: Context = LocalContext.current,
    navController: NavController,
    loginViewModel: LoginViewModel
) {
    val username = loginViewModel.userName.value
    val password = loginViewModel.password.value
    val logo = if (!isSystemInDarkTheme()) R.drawable.logo_dark else R.drawable.logo_light
    val scrollState = rememberScrollState()
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isVisible = true
    }
    val contentAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(600, delayMillis = 100),
        label = "Content alpha"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .verticalScroll(scrollState)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        
        Image(
            painter = painterResource(logo),
            contentDescription = "Logo",
            modifier = Modifier
                .size(160.dp)
                .padding(bottom = 16.dp)
        )

        Text(
            text = "Welcome Back",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.alpha(contentAlpha)
        )

        Text(
            text = "Login to your account to continue",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .padding(bottom = 32.dp)
                .alpha(contentAlpha)
        )

        TextInput(
            value = username,
            onValueChange = { loginViewModel.userName.value = it },
            placeholder = "Username or Email",
            label = "Username or Email",
            leadingIcon = Icons.Rounded.Email,
            modifier = Modifier.alpha(contentAlpha)
        )

        Spacer(modifier = Modifier.height(8.dp))

        PasswordInput(
            value = password,
            onValueChange = { loginViewModel.password.value = it },
            placeholder = "Enter your password",
            label = "Password",
            leadingIcon = Icons.Rounded.Lock,
            modifier = Modifier.alpha(contentAlpha)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .alpha(contentAlpha),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextBoxForm(
                text = stringResource(R.string.remember_me),
                onClick = { loginViewModel.onRememberMeChanged() },
                enabled = loginViewModel.rememberMe.value
            )
            TextFormButton(
                text = stringResource(R.string.forget_password),
                onClick = { navController.navigate(FORGET_PASS_SCREEN_ROUTE) }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        FormButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .alpha(contentAlpha),
            text = stringResource(R.string.login),
            onClick = {
                loginViewModel.login(username = username, password = password)
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .alpha(contentAlpha),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.outlineVariant
            )
            Text(
                text = " OR ",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.outlineVariant
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        GoogleSignInButton(
            modifier = Modifier.alpha(contentAlpha),
            onSignInSuccess = {
                navController.navigate(MAIN_SCREEN_ROUTE) {
                    launchSingleTop = true
                    popUpTo(LOGIN_SCREEN_ROUTE) { inclusive = true }
                }
            },
            onSignInError = {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        )
        Spacer(modifier = Modifier.height(16.dp)) // Minimum Spacing
        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .alpha(contentAlpha),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.don_t_have_an_account),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            TextFormButton(
                text = stringResource(R.string.sign_up),
                onClick = { navController.navigate(REGISTER_SCREEN_ROUTE) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
