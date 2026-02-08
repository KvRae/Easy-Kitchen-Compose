package com.kvrae.easykitchen.presentation.forget_password

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kvrae.easykitchen.presentation.miscellaneous.components.CustomSnackbar
import com.kvrae.easykitchen.presentation.miscellaneous.components.SnackbarType
import com.kvrae.easykitchen.utils.EMAIL_FPS_ROUTE
import com.kvrae.easykitchen.utils.FORGET_PASS_SCREEN_ROUTE
import com.kvrae.easykitchen.utils.LOGIN_SCREEN_ROUTE
import com.kvrae.easykitchen.utils.OTP_FPS_ROUTE
import com.kvrae.easykitchen.utils.PASSWORD_FPS_ROUTE
import com.kvrae.easykitchen.utils.popThenNavigateTo
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgetPasswordScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val viewModel = koinViewModel<ForgetPasswordViewModel>()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val navRoute = remember {
        mutableStateOf(EMAIL_FPS_ROUTE)
    }

    // Function to handle back navigation
    val handleBackPress: () -> Unit = {
        when (navRoute.value) {
            PASSWORD_FPS_ROUTE -> navRoute.value = OTP_FPS_ROUTE
            OTP_FPS_ROUTE -> navRoute.value = EMAIL_FPS_ROUTE
            else -> navController.popThenNavigateTo(
                navigateRoute = LOGIN_SCREEN_ROUTE,
                popRoute = FORGET_PASS_SCREEN_ROUTE
            )
        }
    }

    // Get screen title based on current route
    val screenTitle = when (navRoute.value) {
        PASSWORD_FPS_ROUTE -> "Reset Password"
        OTP_FPS_ROUTE -> "Verify Code"
        else -> "Forgot Password"
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // Top App Bar with Back Button
            TopAppBar(
                title = {
                    Text(
                        text = screenTitle,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = handleBackPress) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )

            // Content
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                when (navRoute.value) {
                    PASSWORD_FPS_ROUTE -> PasswordFPScreen(
                        viewModel = viewModel,
                        onSuccess = {
                            navController.popThenNavigateTo(
                                navigateRoute = LOGIN_SCREEN_ROUTE,
                                popRoute = FORGET_PASS_SCREEN_ROUTE
                            )
                        },
                        onError = { error ->
                            scope.launch {
                                snackbarHostState.showSnackbar(error)
                            }
                        }
                    )

                    OTP_FPS_ROUTE -> OtpFPScreen(
                        viewModel = viewModel,
                        onSuccess = {
                            navRoute.value = PASSWORD_FPS_ROUTE
                        },
                        onError = { error ->
                            scope.launch {
                                snackbarHostState.showSnackbar(error)
                            }
                        }
                    )

                    else -> EmailFPScreen(
                        viewModel = viewModel,
                        onSuccess = {
                            navRoute.value = OTP_FPS_ROUTE
                        },
                        onError = { error ->
                            scope.launch {
                                snackbarHostState.showSnackbar(error)
                            }
                        }
                    )
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) { data ->
            CustomSnackbar(
                data = data,
                type = SnackbarType.ERROR
            )
        }
    }

    BackHandler {
        handleBackPress()
    }
}


