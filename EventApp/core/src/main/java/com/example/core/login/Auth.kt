package com.example.core.login

object Auth {
    var loggedInUserData: LoginUserData? = null
    fun signOut() {
        loggedInUserData = null
    }
}