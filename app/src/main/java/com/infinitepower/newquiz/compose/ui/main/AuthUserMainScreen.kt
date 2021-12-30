package com.infinitepower.newquiz.compose.ui.main

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.infinitepower.newquiz.compose.core.navigation.Screen
import com.infinitepower.newquiz.compose.data.remote.auth.user.AuthUserApi

@Composable
fun AuthUserMainScreen(
    navController: NavHostController,
    authUserApi: AuthUserApi
) {
    val isSignedIn = authUserApi.isSignedIn

    if (isSignedIn) {
        SignedAuthUserContent(
            name = authUserApi.getName().orEmpty(),
            photoUri = authUserApi.getPhotoUrl()
        )
    } else {
        SignUpUserContent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            onSignInClick = {
                navController.navigate(Screen.LoginScreen.route)
            }
        )
    }
}

@Composable
private fun SignedAuthUserContent(
    name: String,
    photoUri: Uri?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            AsyncImage(
                model = photoUri,
                contentDescription = "Profile Image",
                modifier = Modifier
                    .size(75.dp)
                    .clip(CircleShape)
                    .border(3.dp, Color.White, CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Good Day",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = name,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun SignUpUserContent(
    modifier: Modifier = Modifier,
    onSignInClick: () -> Unit
) {
    Surface(
        modifier = modifier,
        tonalElevation = 8.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Text(
                text = "Save your progress, buy skips and see leaderboard by signing in!",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                onClick = onSignInClick,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(text = "Sign In")
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun SignedAuthUserContentPreview() {
    SignedAuthUserContent(name = "NewQuiz", photoUri = null)
}

@Composable
@Preview(showBackground = true)
private fun SignUpUserContentPreview() {
    SignUpUserContent(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        onSignInClick = {}
    )
}