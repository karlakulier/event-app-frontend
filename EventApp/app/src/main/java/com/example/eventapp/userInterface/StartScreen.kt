package com.example.eventapp.userInterface

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.example.eventapp.R
import com.google.android.gms.common.SignInButton
import androidx.compose.ui.viewinterop.AndroidView
import com.example.eventapp.MainActivity
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.CallbackManager
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.FacebookCallback

@Composable
fun StartScreen(
    modifier: Modifier = Modifier,
    onSignUpClick: () -> Unit,
    onLoginClick: () -> Unit,
    onGoogleLoginClick: () -> Unit,
    onFacebookLoginClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.app_logo),
            contentDescription = stringResource(id = R.string.app_logo),
            modifier = Modifier.size(280.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        //Text(text = stringResource(id = R.string.existing_user))
        Button(
            onClick = onLoginClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .height(52.dp)
        ) {
            Text(text = stringResource(id = R.string.login))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = stringResource(id = R.string.new_user))
        Button(
            onClick = onSignUpClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .height(52.dp)
        ) {
            Text(text = stringResource(id = R.string.sign_up))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Divider(modifier = Modifier.weight(1f), thickness = 1.dp)
            Text(text = stringResource(id = R.string.or), modifier = Modifier.padding(horizontal = 8.dp))
            Divider(modifier = Modifier.weight(1f), thickness = 1.dp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        GoogleSignInButton(
            context = LocalContext.current,
            onSignInClick = onGoogleLoginClick
        )
        Spacer(modifier = Modifier.height(16.dp))

        FacebookSignInButton(
            context = LocalContext.current,
            onSignInClick = onFacebookLoginClick
        )
    }
}

@Composable
fun GoogleSignInButton(
    context: Context,
    onSignInClick: () -> Unit
) {
    AndroidView(
        factory = { ctx ->
            SignInButton(ctx).apply {
                setSize(SignInButton.SIZE_WIDE)
                setOnClickListener { onSignInClick() }
            }
        }
    )
}

@Composable
fun FacebookSignInButton(
    context: Context,
    onSignInClick: () -> Unit
) {
    AndroidView(
        factory = { ctx ->
            com.facebook.login.widget.LoginButton(ctx).apply {
                setPermissions("email", "public_profile")
                setOnClickListener { onSignInClick() }
            }
        }
    )
}