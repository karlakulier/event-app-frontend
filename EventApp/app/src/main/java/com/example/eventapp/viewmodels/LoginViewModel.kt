package com.example.eventapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.login.Auth
import com.example.core.login.LoginHandler
import com.example.core.login.LoginOutcomeListener
import com.example.core.login.LoginToken
import com.example.core.login.LoginUserData
import com.example.core.login.MusicianData
import com.example.core.login.OrganizerData
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
                    Auth.loggedInUserData = LoginUserData(
                        id = loginUserData.id,
                        username = loginUserData.username,
                        role = loginUserData.role,
                        organizer = loginUserData.organizer?.let {
                            OrganizerData(
                                id = it.id,
                                name = it.name,
                                surname = it.surname,
                                email = it.email,
                                contact = it.contact,
                                birthDate = it.birthDate,
                                userId = it.userId
                            )
                        },
                        musician = loginUserData.musician?.let {
                            MusicianData(
                                id = it.id,
                                name = it.name,
                                surname = it.surname,
                                email = it.email,
                                contact = it.contact,
                                description = it.description,
                                artistName = it.artistName,
                                userId = it.userId
                            )
                        }
                    )
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