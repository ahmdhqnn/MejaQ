package org.d3ifcool.admin.keuangan

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import org.d3ifcool.shared.model.Transaksi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KeuanganScreen(
    navController: NavHostController,
    viewModel: MainViewModel = viewModel()
) {
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

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            items(viewModel.data) { transaksi ->
                RiwayatCard(transaksi)
            }
        }
    }
}

@Composable
fun RiwayatCard(transaksi: Transaksi) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEEF2))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {

            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = transaksi.meja, fontWeight = FontWeight.Bold)
                    Text(
                        text = "${transaksi.tanggal} • ${transaksi.waktu}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
                TextButton(onClick = { expanded = !expanded }) {
                    Text(if (expanded) "Tutup" else "Lihat")
                }
            }

            // Detail ditampilkan saat expanded
            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Pelanggan: ${transaksi.namaPelanggan}",
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(text = transaksi.nama)   // nama menu
                        Text(
                            text = "Jumlah: ${transaksi.jumlah}",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }

                    Text(
                        text = "Rp ${transaksi.total}",
                        fontWeight = FontWeight.Bold
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
