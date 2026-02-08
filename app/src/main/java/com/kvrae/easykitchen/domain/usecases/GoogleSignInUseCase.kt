package com.kvrae.easykitchen.domain.usecases

import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.kvrae.easykitchen.data.remote.dto.User
import com.kvrae.easykitchen.data.repository.AuthRepository
import java.security.SecureRandom

// Build a Google ID token credential request using Credential Manager (Materialized from server client ID in strings.xml)
class BuildGoogleCredentialRequestUseCase {
    operator fun invoke(serverClientId: String): GetCredentialRequest {
        // Generate a random nonce for security and to force fresh auth
        val nonce = generateNonce()

        // Use GetSignInWithGoogleOption which forces a fresh sign-in flow
        // This bypasses cached credentials and reauth loops
        val googleOption = GetSignInWithGoogleOption.Builder(serverClientId)
            .setNonce(nonce)
            .build()

        return GetCredentialRequest.Builder()
            .addCredentialOption(googleOption)
            .build()
    }

    private fun generateNonce(): String {
        // Generate a secure random nonce
        val ranNonce = ByteArray(16)
        SecureRandom().nextBytes(ranNonce)
        return ranNonce.joinToString("") { "%02x".format(it) }
    }
}

// Handle the Google credential flow via repository (suspending). Returns a User mapped from the ID token/profile.
class HandleGoogleCredentialResultUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(request: GetCredentialRequest): Result<User> {
        return authRepository.getGoogleUserFromCredentials(request)
    }
}
