package com.kvrae.easykitchen.data.repository

import android.app.Activity
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.kvrae.easykitchen.R
import com.kvrae.easykitchen.data.remote.dto.User
import com.kvrae.easykitchen.utils.GOOGLE_LOGIN_URL
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess

interface AuthRepository {
    fun getGoogleSignInClient(activity: Activity): GoogleSignInClient
    fun getSignInIntent(googleSignInClient: GoogleSignInClient): Intent
    fun handleSignInResult(completedTask: Task<GoogleSignInAccount>): Result<String> // Return ID token
    suspend fun sendIdTokenToBackend(idToken: String): Result<User> // Send token to your backend
}

class AuthRepositoryImpl(private val client: HttpClient) : AuthRepository {

    override fun getGoogleSignInClient(activity: Activity): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.getString(R.string.google_client_id))
            .requestEmail()
            .requestProfile()
            .build()
        return GoogleSignIn.getClient(activity, gso)
    }

    override fun getSignInIntent(googleSignInClient: GoogleSignInClient): Intent {
        return googleSignInClient.signInIntent
    }

    override fun handleSignInResult(completedTask: Task<GoogleSignInAccount>): Result<String> {
        return try {
            val account = completedTask.getResult(ApiException::class.java)
            val idToken = account.idToken ?: throw Exception("ID token is null")
            Result.success(idToken)
        } catch (e: ApiException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun sendIdTokenToBackend(idToken: String): Result<User> {
        return try {
            val response: HttpResponse = client.post(GOOGLE_LOGIN_URL) {
                contentType(ContentType.Application.Json)
                setBody(idToken)
            }
            if (response.status.isSuccess()) {
                val user = User() // Parse the response body to User object
                Result.success(user)
            } else {
                Result.failure(Exception("Backend authentication failed with status: ${response.status.value}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}