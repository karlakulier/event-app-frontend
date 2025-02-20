package com.example.core.login

interface LoginHandler {
    fun handleLogin(loginToken: LoginToken, loginListener: LoginOutcomeListener)
}