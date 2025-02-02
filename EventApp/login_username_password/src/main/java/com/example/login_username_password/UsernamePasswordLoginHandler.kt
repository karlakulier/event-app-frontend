package com.example.login_username_password

import android.util.Log
import com.example.core.login.LoginHandler
import com.example.core.login.LoginOutcomeListener
import com.example.core.login.LoginToken
import com.example.core.login.LoginUserData
import com.example.core.login.MusicianData
import com.example.core.login.OrganizerData
import com.example.core.network.ResponseListener
import com.example.core.network.models.ErrorResponseBody
import com.example.core.network.models.SuccessfulResponseBody
import com.example.ws.models.LoggedInUserData
import com.example.ws.models.LoginBody
import com.example.ws.request_handlers.LoginRequestHandler

class UsernamePasswordLoginHandler : LoginHandler {
    override fun handleLogin(
        loginToken: LoginToken,
        loginListener: LoginOutcomeListener
    ) {
        if (loginToken !is UsernamePasswordLoginToken) {
            throw IllegalArgumentException("Must receive UsernamePasswordLoginToken instance for 'loginToken'!")
        }

        val authorizers = loginToken.getAuthorizers()
        val username = authorizers["username"]!!
        val password = authorizers["password"]!!

        val loginRequestHandler = LoginRequestHandler(LoginBody(username, password))

        loginRequestHandler.sendRequest(
            object : ResponseListener<LoggedInUserData> {
                override fun onSuccessfulResponse(response: LoggedInUserData) {
                    val loginUserData = response

                    loginListener.onSuccessfulLogin(
                        LoginUserData(
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
                    )
                }

                override fun onErrorResponse(response: ErrorResponseBody) {
                    loginListener.onFailedLogin(response.message)
                }

                override fun onNetworkFailure(t: Throwable) {
                    loginListener.onFailedLogin(t.message ?: "Could not connect to network.")
                }
            }
        )
    }
}