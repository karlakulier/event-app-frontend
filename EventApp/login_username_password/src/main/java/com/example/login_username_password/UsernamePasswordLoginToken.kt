package com.example.login_username_password

import com.example.core.login.LoginToken

class UsernamePasswordLoginToken(username: String, password: String) : LoginToken {
    private val authorizers = mapOf(
        "username" to username,
        "password" to password
    )

    override fun getAuthorizers(): Map<String, String> {
        return authorizers
    }
}