package com.example.eventapp.utils

import androidx.core.util.PatternsCompat
import java.util.regex.Pattern

//Provjera valjanosti passworda
fun isPasswordValid(password: String): Boolean {
    val passwordPattern = Pattern.compile("^(?=.*[0-9])(?=.*[A-Z]).{8,}$")
    return passwordPattern.matcher(password).matches()
}

//Provjera valjanosti e-maila
fun isEmailValid(email: String): Boolean {
    return PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()
}
