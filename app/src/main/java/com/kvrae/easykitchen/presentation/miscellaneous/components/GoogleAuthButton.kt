package com.kvrae.easykitchen.presentation.miscellaneous.components



import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes
import com.google.android.gms.common.api.ApiException
import com.kvrae.easykitchen.R
import com.kvrae.easykitchen.data.remote.dto.User
import com.kvrae.easykitchen.presentation.login.GoogleAuthState
import com.kvrae.easykitchen.presentation.login.GoogleAuthViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun GoogleSignInButton(
    onSignInSuccess: (User) -> Unit,
    onSignInError: (String) -> Unit
) {
    val context = LocalContext.current as Activity
    val viewModel = koinViewModel<GoogleAuthViewModel>()
    val googleSignInClient = viewModel.getGoogleSignInClient(context)

    val signInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result: ActivityResult ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                task.getResult(ApiException::class.java)
                viewModel.handleSignInResult(task)
            } catch (e: ApiException) {

                val errorMessage = when (e.statusCode) {
                    GoogleSignInStatusCodes.SIGN_IN_CANCELLED -> context.getString(R.string.sign_in_cancelled_by_user)
                    GoogleSignInStatusCodes.NETWORK_ERROR -> "Network error, please try again"
                    else -> "Failed: Got to pay money for the api"
                }
                onSignInError(errorMessage)
            }
        }
    )

    val googleAuthState by viewModel.googleAuthState.collectAsState()

    LaunchedEffect(key1 = googleAuthState) {
        when (googleAuthState) {
            is GoogleAuthState.Success -> {
                onSignInSuccess((googleAuthState as GoogleAuthState.Success).user)
            }
            is GoogleAuthState.Error -> {
                onSignInError((googleAuthState as GoogleAuthState.Error).message)
            }
            else -> {}
        }
    }

    Button(onClick = {
        viewModel.resetGoogleAuthState() // Clear previous state before sign-in
        val signInIntent = viewModel.getSignInIntent(googleSignInClient)
        signInLauncher.launch(signInIntent)
    }) {
        Text("Sign in with Google")
    }
}