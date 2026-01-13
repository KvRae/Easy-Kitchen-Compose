package com.kvrae.easykitchen.presentation.register

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kvrae.easykitchen.R
import com.kvrae.easykitchen.presentation.miscellaneous.components.FormButton
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
            value = viewModel.username.value,
            onValueChange = {
                viewModel.username.value = it
            },
            placeholder = "Enter Your Username",
            label = "Username",
            leadingIcon = Icons.Rounded.AccountCircle,
        )
        TextInput(
            value = viewModel.email.value,
            onValueChange = {
                viewModel.email.value = it
            },
            placeholder = "Enter Your Email",
            label = "Email",
            leadingIcon = Icons.Rounded.Email,
        )
        TextInput(
            value = viewModel.password.value,
            onValueChange = {
                viewModel.password.value = it
            },
            placeholder = "Enter Your Password",
            label = "Password",
            leadingIcon = Icons.Rounded.Lock,
        )

        TextInput(
            placeholder = "Repeat Your Password",
            label = "Repeat Password",
            leadingIcon = Icons.Rounded.Lock,
        )

        FormButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            text = "Sign Up",
            onClick = {
                viewModel.register()
            }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "Already have an account?",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            TextFormButton(
                text = "Login",
                onClick = {
                    onLoginClick
                }
            )
        }
    }
}