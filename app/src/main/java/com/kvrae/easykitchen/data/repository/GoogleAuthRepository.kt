package com.kvrae.easykitchen.data.repository

import android.content.Context
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.kvrae.easykitchen.data.remote.dto.User

/**
 * Repository for handling Google Sign-In via modern Credential Manager + Google Identity Services.
 * This mobile-first implementation retrieves an ID token using Credential Manager.
 * If you need verified profile (email/displayName), validate the token on a backend.
 */
interface AuthRepository {
    suspend fun getGoogleUserFromCredentials(request: GetCredentialRequest): Result<User>
}

class AuthRepositoryImpl(private val context: Context) : AuthRepository {

    override suspend fun getGoogleUserFromCredentials(request: GetCredentialRequest): Result<User> {
        return try {
            Log.d("AuthRepositoryImpl", "Starting credential retrieval...")
            val credentialManager = CredentialManager.create(context)
            Log.d("AuthRepositoryImpl", "CredentialManager created successfully")

            Log.d(
                "AuthRepositoryImpl",
                "Calling getCredential with context: ${context.javaClass.simpleName}"
            )
            val response: GetCredentialResponse =
                credentialManager.getCredential(context = context, request = request)
            Log.d(
                "AuthRepositoryImpl",
                "Credential response received, type: ${response.credential.type}"
            )

            Log.d("AuthRepositoryImpl", "Creating GoogleIdTokenCredential from response data")
            val credential = GoogleIdTokenCredential.createFrom(response.credential.data)
            Log.d("AuthRepositoryImpl", "GoogleIdTokenCredential created from response")

            val idToken = credential.idToken
            val displayName = credential.displayName
            val email = credential.id // This is the email address

            Log.d(
                "AuthRepositoryImpl",
                "User email: $email, displayName: $displayName, idToken length: ${idToken.length}"
            )

            val user = User(
                email = email, // Use the actual email instead of ID token
                username = displayName
                    ?: email.substringBefore("@") // Use display name or email prefix
            )
            Log.d("AuthRepositoryImpl", "User object created successfully: $user")
            Result.success(user)
        } catch (e: GetCredentialCancellationException) {
            // User canceled OR provider requires re-auth (e.g., error 16). Clear any cached session.
            Log.w(
                "AuthRepositoryImpl",
                "GetCredentialCancellationException caught: ${e.type}, message: ${e.message}",
                e
            )
            runCatching {
                CredentialManager.create(context)
                    .clearCredentialState(ClearCredentialStateRequest())
                Log.d("AuthRepositoryImpl", "Credential state cleared successfully")
            }.onFailure {
                Log.e("AuthRepositoryImpl", "Failed to clear credential state", it)
            }
            // Surface the original exception so UI can show a friendly message without crashing
            Result.failure(e)
        } catch (e: GetCredentialException) {
            // Surface credential-specific errors (e.g., reauth/account issues)
            val message = "Credential error: ${e.javaClass.simpleName} (${e.type})"
            Log.e("AuthRepositoryImpl", message, e)
            Result.failure(e)
        } catch (e: Exception) {
            Log.e(
                "AuthRepositoryImpl",
                "Unexpected error getting credentials: ${e.javaClass.simpleName} - ${e.message}",
                e
            )
            e.printStackTrace()
            Result.failure(e)
        }
    }
}