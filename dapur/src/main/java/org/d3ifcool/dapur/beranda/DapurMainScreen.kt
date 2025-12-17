package org.d3ifcool.dapur.beranda

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3ifcool.dapur.navigation.Screen
import org.d3ifcool.dapur.theme.MejaQTheme
import org.d3ifcool.shared.R
import org.d3ifcool.shared.model.Pesanan

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DapurMainScreen(
    navController: NavHostController,
    viewModel: MainViewModel = viewModel()
) {
    Scaffold(
        containerColor = Color(0xFFFDFDFE),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Pesanan",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
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
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {

            // list pesanan
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(viewModel.data) { pesanan ->
                    PesananCard(
                        pesanan = pesanan,
                        navController = navController,
                        onPesananSelesai = {
                            navController.navigate(Screen.Success.route)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Navbar
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PesananCard(
    pesanan: Pesanan,
    navController: NavHostController,
    onPesananSelesai: (Pesanan) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    var statusExpanded by remember { mutableStateOf(false) }
    val statusOptions = listOf("Pending", "Ready")
    var selectedStatus by remember { mutableStateOf(pesanan.status) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEEF2))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = pesanan.meja, fontWeight = FontWeight.Bold)
                    Text(
                        text = "${pesanan.tanggal} • ${pesanan.waktu}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                TextButton(onClick = { expanded = !expanded }) {
                    Text(if (expanded) "Tutup" else "Lihat")
                }
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))

                pesanan.daftarMenu.forEach { item ->
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = item.nama, fontWeight = FontWeight.SemiBold)
                            Text(text = "x${item.jumlah}")
                        }

                        if (item.catatan.isNotBlank()) {
                            Text(
                                text = "• ${item.catatan}",
                                fontSize = 12.sp,
                                color = Color(0xFF7A7A7A),
                                modifier = Modifier.padding(start = 4.dp, top = 2.dp, bottom = 6.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                ExposedDropdownMenuBox(
                    expanded = statusExpanded,
                    onExpandedChange = { statusExpanded = !statusExpanded }
                ) {

                    OutlinedTextField(
                        value = selectedStatus,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Status Pesanan") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = statusExpanded
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()

                    )

                    ExposedDropdownMenu(
                        expanded = statusExpanded,
                        onDismissRequest = { statusExpanded = false }
                    ) {
                        statusOptions.forEach { status ->
                            DropdownMenuItem(
                                text = { Text(status) },
                                onClick = {
                                    selectedStatus = status
                                    pesanan.status = status
                                    statusExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { showDialog = true },
                    modifier = Modifier.align(Alignment.End),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFB0004D)
                    )
                ) {
                    Text("Konfirmasi", color = Color.White)
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Konfirmasi") },
            text = { Text("Apakah pesanan benar sudah selesai?") },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    onPesananSelesai(pesanan)
                }) {
                    Text("Selesai")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Belum")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BerandaAdminPreview() {
    MejaQTheme {
        DapurMainScreen(rememberNavController())
    }
}
