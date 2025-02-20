package com.example.core.login

interface LoginToken {
    fun getAuthorizers(): Map<String, String>
}