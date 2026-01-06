package org.d3ifcool.mejaq.ui.beranda

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseUser
import org.d3ifcool.mejaq.navigation.Screen
import org.d3ifcool.mejaq.ui.riwayat.BottomNavigationBar
import org.d3ifcool.shared.screen.UserProfileCard
import org.d3ifcool.shared.viewmodel.EventViewModel
import org.d3ifcool.shared.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BerandaUserScreen(
    user: FirebaseUser,
    navController: NavHostController,
    eventViewModel: EventViewModel = viewModel()
) {
    val uiState by eventViewModel.uiState.collectAsState()

    Scaffold(
        containerColor = Color(0xFFFDFDFE),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logo_mejaq),
                            contentDescription = "Logo",
                            modifier = Modifier.size(120.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0xFFFDFDFE)
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 12.dp)
        ) {

            UserProfileCard(user)

            Text(
                text = "Selamat datang, ${user.displayName ?: "User"}!",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.activeEvents) { event ->
                    EventCard(
                        event = event,
                        onClick = {
                            navController.navigate(
                                Screen.DetailEvent.createRoute(event.id)
                            )
                        }
                    )
                }
            }
        }
    }
}
