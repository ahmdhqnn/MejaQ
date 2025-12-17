package org.d3ifcool.mejaq.ui.beranda

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseUser
import org.d3ifcool.mejaq.R
import org.d3ifcool.mejaq.navigation.Screen
import org.d3ifcool.mejaq.ui.theme.MejaqAppTheme
import org.d3ifcool.shared.model.Catatan
import org.d3ifcool.shared.screen.UserProfileCard


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BerandaUserScreen(
    user: FirebaseUser,
    navController: NavHostController,
    viewModel: CartViewModel = viewModel()
) {
    Scaffold(
        containerColor = Color(0xFFFDFDFE),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logo_mejaq),
                            contentDescription = "Logo MejaQ",
                            modifier = Modifier.size(120.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0xFFFDFDFE)
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 12.dp)
        ) {
            UserProfileCard(user)

            Text(
                text = "Selamat datang, ${user.displayName ?: "User"}!",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(viewModel.data) { catatan ->
                    ListItemCard(catatan, navController)
                }
            }

            // ===== MENU BUTTON =====
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(
                    onClick = { navController.navigate(Screen.Home.route) },
                    modifier = Modifier.size(96.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.button_beranda),
                        contentDescription = "Beranda",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                IconButton(
                    onClick = { navController.navigate(Screen.QrCodeScanner.route) },
                    modifier = Modifier.size(72.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.button_scanner),
                        contentDescription = "Scan",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                IconButton(
                    onClick = { navController.navigate(Screen.Riwayat.route) },
                    modifier = Modifier.size(96.dp)
                ) {
                    Image(
                        painter = painterResource(
                            id = org.d3ifcool.shared.R.drawable.button_riwayat
                        ),
                        contentDescription = "Riwayat",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
fun ListItemCard(catatan: Catatan, navController: NavHostController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate(
                    Screen.DetailEvent.createRoute(catatan.id)
                )
            },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(1.dp, DividerDefaults.color),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = catatan.foto),
                contentDescription = catatan.catatan,
                modifier = Modifier
                    .size(96.dp)
                    .padding(end = 12.dp),
                contentScale = ContentScale.Crop
            )
            Column {
                Text(
                    text = catatan.catatan,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = catatan.tanggal,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFFD61355)
                    )
                )
            }
        }
    }
}

