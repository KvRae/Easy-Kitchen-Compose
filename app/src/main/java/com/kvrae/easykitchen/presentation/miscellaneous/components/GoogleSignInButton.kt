package com.kvrae.easykitchen.presentation.miscellaneous.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kvrae.easykitchen.data.remote.dto.User

/**
 * Alias for GoogleAuthButton to maintain backward compatibility.
 * Use this composable for Google Sign-In functionality.
 */
@Composable
fun GoogleSignInButton(
    modifier: Modifier = Modifier,
    onSignInSuccess: (User) -> Unit,
    onSignInError: (String) -> Unit
) {
    GoogleAuthButton(
        modifier = modifier,
        onSignInSuccess = onSignInSuccess,
        onSignInError = onSignInError
    )
}
