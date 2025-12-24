package org.d3ifcool.admin.keuangan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3ifcool.admin.ui.theme.MejaQTheme
import org.d3ifcool.shared.screen.LayoutPage
import org.d3ifcool.shared.viewmodel.PesananViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api:: class)
@Composable
fun KeuanganScreen(
    navController: NavHostController,
    pesananViewModel: PesananViewModel = viewModel()
) {
    val uiState by pesananViewModel.uiState. collectAsState()

    // Load completed orders (transaksi) when screen opens
    LaunchedEffect(Unit) {
        pesananViewModel. loadCompletedPesanan()
    }

    Scaffold(
        containerColor = Color(0xFFFDFDFE),
        topBar = {
            TopAppBar(
                title = { LayoutPage(navController = navController) },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0xFFFDFDFE),
                    titleContentColor = Color(0xFFD61355),
                )
            )
        }
    ) { innerPadding ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    . fillMaxSize()
                    . padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFFD61355))
            }
        } else if (uiState.completedPesanan.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Belum ada transaksi",
                    color = Color.Gray
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement. spacedBy(8.dp)
            ) {
                // Summary Card
                item {
                    val totalPendapatan = uiState.completedPesanan.sumOf { it.totalHarga }
                    SummaryTransaksiCard(
                        totalTransaksi = uiState.completedPesanan.size,
                        totalPendapatan = totalPendapatan
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Riwayat Transaksi",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                items(uiState.completedPesanan) { pesanan ->
                    RiwayatCard(pesanan)
                }
            }
        }
    }
}

@Composable
fun SummaryTransaksiCard(
    totalTransaksi: Int,
    totalPendapatan:  Int
) {
    val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))

    Card(
        modifier = Modifier. fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFD61355))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Total Transaksi",
                    color = Color.White. copy(alpha = 0.8f),
                    fontSize = 12.sp
                )
                Text(
                    text = "$totalTransaksi",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Total Pendapatan",
                    color = Color. White.copy(alpha = 0.8f),
                    fontSize = 12.sp
                )
                Text(
                    text = formatter.format(totalPendapatan),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
        }
    }
}

@Composable
fun RiwayatCard(pesanan:  org.d3ifcool.shared.model.Pesanan) {
    var expanded by remember { mutableStateOf(false) }
    val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))

    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = { expanded = !expanded },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEEF2))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = pesanan.meja,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${pesanan.tanggal} • ${pesanan.waktu}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
                Text(
                    text = if (expanded) "Tutup" else "Lihat",
                    color = Color(0xFFD61355),
                    fontSize = 14.sp
                )
            }

            // Detail saat expanded
            if (expanded) {
                Spacer(modifier = Modifier.height(12.dp))
                Divider()
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Pelanggan:  ${pesanan.namaPelanggan}",
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier. height(8.dp))

                // Daftar menu
                pesanan. daftarMenu.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement. SpaceBetween
                    ) {
                        Column {
                            Text(text = item.nama)
                            if (item.catatan.isNotEmpty()) {
                                Text(
                                    text = "• ${item.catatan}",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = "x${item.jumlah}")
                            Text(
                                text = formatter.format(item.subtotal),
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Divider()
                Spacer(modifier = Modifier. height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Total",
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = formatter. format(pesanan.totalHarga),
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFD61355)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewKeuanganScreen() {
    MejaQTheme {
        KeuanganScreen(rememberNavController())
    }
}