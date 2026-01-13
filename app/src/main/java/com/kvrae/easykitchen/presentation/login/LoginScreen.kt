package com.kvrae.easykitchen.presentation.login

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kvrae.easykitchen.R
import com.kvrae.easykitchen.presentation.meals.MealsViewModel
import com.kvrae.easykitchen.presentation.miscellaneous.components.FormButton
import com.kvrae.easykitchen.presentation.miscellaneous.components.GoogleSignInButton
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
    val loginViewModel= koinViewModel<LoginViewModel>()
    val loginState = loginViewModel.loginState.collectAsState().value
    val mealsViewModel = koinViewModel<MealsViewModel>()
    val context = LocalContext.current

    LoginUILayout(
        navController = navController,
        loginViewModel = loginViewModel
    )

    when (loginState) {
        is LoginState.Loading -> LoadingTransparentScreen()
        is LoginState.Success -> {
            LaunchedEffect(Unit) {
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
                Toast.makeText(context, loginState.message, Toast.LENGTH_SHORT).show()
                loginViewModel.resetLoginState()
            }
        }
        else -> Unit
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.ic_launcher_foreground),
            contentDescription = "Logo"
        )
        TextInput(
            value = username,
            onValueChange = {
                loginViewModel.userName.value = it
            },
            placeholder = "Enter a username / email",
            label = "Username or Email",
            leadingIcon = Icons.Rounded.Email,
        )
        TextInput(
            value = password,
            onValueChange = {
                loginViewModel.password.value = it
            },
            placeholder = "Enter your password",
            label = "Password",
            leadingIcon = Icons.Rounded.Lock,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextBoxForm(
                text = stringResource(R.string.remember_me),
                onClick = {
                    loginViewModel.onRememberMeChanged()
                },
                enabled = loginViewModel.rememberMe.value

            )
            TextFormButton(
                text = stringResource(R.string.forget_password),
                onClick = {
                    navController.navigate(FORGET_PASS_SCREEN_ROUTE)
                }
            )
        }

        FormButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            text = stringResource(R.string.login),
            onClick = {
                loginViewModel.login(
                    username = username,
                    password = password
                )
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        GoogleSignInButton(
            onSignInSuccess = {
                navController.navigate(MAIN_SCREEN_ROUTE) {
                    launchSingleTop = true
                    popUpTo(LOGIN_SCREEN_ROUTE) {
                        inclusive = true
                    }
                }
            },
            onSignInError = {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }

        )

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.don_t_have_an_account),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            TextFormButton(
                text = stringResource(R.string.sign_up),
                onClick = {
                    navController.navigate(REGISTER_SCREEN_ROUTE)
                }
            )
        }

    }

}