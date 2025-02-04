package com.example.core.googleLogin

import com.example.core.login.LoginToken

data class GoogleLoginToken(
    val idToken: String,
    val email: String? = null
) : LoginToken {
    override fun getAuthorizers(): Map<String, String> {
        return mapOf("Authorization" to "Bearer $idToken")
    }
}