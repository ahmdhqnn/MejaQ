package org.d3ifcool.dapur.beranda

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import org.d3ifcool.dapur.navigation.Screen
import org.d3ifcool.shared.R
import org.d3ifcool.shared.model.Pesanan

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RiwayatScreen(
    navController: NavHostController,
    viewModel: MainViewModel = viewModel()
) {
    Scaffold(
        containerColor = Color(0xFFFDFDFE),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Riwayat",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                    )
                },
                actions = {
                    Image(
                        painter = painterResource(id = R.drawable.logo_mejaq),
                        contentDescription = "Logo MejaQ",
                        modifier = Modifier
                            .size(90.dp)
                            .padding(end = 12.dp)
                    )
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0xFFFDFDFE),
                    titleContentColor = Color(0xFF6A1B9A),
                )
            )

        }
    )  { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {

            // --- LIST RIWAYAT ---
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(viewModel.data) { pesanan ->
                    RiwayatCard(pesanan)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // --- TOMBOL GAMBAR BAWAH ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                IconButton(
                    onClick = { navController.navigate(Screen.Home.route) },
                    modifier = Modifier.size(150.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.button_pesanan),
                        contentDescription = "Pesanan",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                IconButton(
                    onClick = { navController.navigate(Screen.Riwayat.route) },
                    modifier = Modifier.size(150.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.button_riwayat),
                        contentDescription = "Riwayat",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
fun RiwayatCard(pesanan: Pesanan) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFEEF2)
        )
    ) {
        Column(Modifier.padding(12.dp)) {

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(pesanan.meja, fontWeight = FontWeight.Bold)
                    Text(
                        "${pesanan.tanggal} • ${pesanan.waktu}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
                TextButton(onClick = { expanded = !expanded }) {
                    Text(if (expanded) "Tutup" else "Lihat")
                }
            }

            if (expanded) {
                Spacer(Modifier.height(8.dp))

                pesanan.daftarMenu.forEach { item ->
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(item.nama)
                        Text("x${item.jumlah}")
                    }
                }
            }
        }
    }
}
