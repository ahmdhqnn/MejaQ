package org.d3ifcool.mejaq.login

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.shape.RoundedCornerShape
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract

import org.d3ifcool.mejaq.R
import org.d3ifcool.mejaq.navigation.Screen
import org.d3ifcool.mejaq.ui.theme.MejaqAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavHostController
) {
    val contract = FirebaseAuthUIActivityResultContract()
    val launcher = rememberLauncherForActivityResult(contract) { }
    Box(modifier = Modifier.fillMaxSize()) {

        // ===== BACKGROUND =====
        Image(
            painter = painterResource(id = org.d3ifcool.shared.R.drawable.background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // ===== CONTENT =====
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // LOGO
            Image(
                painter = painterResource(id = R.drawable.logo_mejaq),
                contentDescription = "Logo MejaQ",
                modifier = Modifier
                    .size(250.dp)
                    .padding(bottom = 16.dp)
            )

            // TEXT
            Text(
                text = stringResource(id = org.d3ifcool.mejaq.R.string.kalimat),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // BUTTON LOGIN (TIDAK PANJANG)
            Button(
                onClick = {
                    launcher.launch(getSigninIntent())
                },
                modifier = Modifier
                    .width(180.dp)   // 🔥 bikin tombol pendek
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD61355)
                ),
                shape = RoundedCornerShape(50) // opsional: biar pill
            ) {
                Text(
                    text = "Login",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

private fun getSigninIntent(): Intent {
    return AuthUI.getInstance()
        .createSignInIntentBuilder()
        .setAvailableProviders(
            arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())
        )
        .build()
}


@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    MejaqAppTheme {
        LoginScreen(rememberNavController())
    }
}