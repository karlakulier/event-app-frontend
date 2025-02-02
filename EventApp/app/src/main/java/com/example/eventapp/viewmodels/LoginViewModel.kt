package com.example.eventapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.login.LoginHandler
import com.example.core.login.LoginOutcomeListener
import com.example.core.login.LoginToken
import com.example.core.login.LoginUserData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    fun updateUsername(value: String) {
        _username.value = value
    }

    fun updatePassword(value: String) {
        _password.value = value
    }

    fun login(
        loginHandler: LoginHandler,
        loginToken: LoginToken,
        onSuccessfulLogin: () -> Unit,
        onFailedLogin: () -> Unit
    ) {
        viewModelScope.launch {
            loginHandler.handleLogin(loginToken, object : LoginOutcomeListener {
                override fun onSuccessfulLogin(loginUserData: LoginUserData) {
                    // Ovdje možeš spremiti korisnika ili token ako treba
                    onSuccessfulLogin()
                }

                override fun onFailedLogin(reason: String) {
                    _errorMessage.value = reason
                    onFailedLogin()
                }
            })
        }
    }
}