package com.kvrae.easykitchen.presentation.miscellaneous.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kvrae.easykitchen.R
import com.kvrae.easykitchen.data.remote.dto.User
import com.kvrae.easykitchen.presentation.login.GoogleAuthState
import com.kvrae.easykitchen.presentation.login.GoogleAuthViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun GoogleAuthButton(
    modifier: Modifier = Modifier,
    onSignInSuccess: (User) -> Unit,
    onSignInError: (String) -> Unit
) {
    val viewModel = koinViewModel<GoogleAuthViewModel>()
    val googleAuthState by viewModel.googleAuthState.collectAsState()
    val clientId = stringResource(R.string.default_web_client_id)

    LaunchedEffect(googleAuthState) {
        when (googleAuthState) {
            is GoogleAuthState.Success -> {
                onSignInSuccess((googleAuthState as GoogleAuthState.Success).user)
                viewModel.resetGoogleAuthState()
            }
            is GoogleAuthState.Error -> {
                onSignInError((googleAuthState as GoogleAuthState.Error).message)
                viewModel.resetGoogleAuthState()
            }
            else -> {}
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 1.5.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(12.dp)
            )
            .background(MaterialTheme.colorScheme.surface)
            .clickable {
                viewModel.signIn(clientId)
            },
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Google Icon
            Icon(
                painter = painterResource(R.drawable.ic_google),
                contentDescription = "Google Logo",
                modifier = Modifier.size(20.dp),
                tint = Color.Unspecified
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Text
            Text(
                text = "Continue with Google",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}