package org.d3ifcool.mejaq.ui.keranjang

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import org.d3ifcool.mejaq.navigation.Screen
import org.d3ifcool.shared.viewmodel.PesananViewModel

private val Merah = Color(0xFFD61355)
private val MerahMuda = Color(0xFFFFEEF2)

@Composable
fun CartScreen(
    navController: NavHostController,
    tableNumber: String?,
    viewModel: PesananViewModel
) {
    Column(Modifier.fillMaxSize()) {

        Text(
            text = "Ringkasan Pesanan",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp),
            color = Merah
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(viewModel.cartItems) { item ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MerahMuda)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(item.nama, fontWeight = FontWeight.Bold)
                        Text("Jumlah: ${item.jumlah}")
                        if (item.catatan.isNotBlank()) {
                            Text("Catatan: ${item.catatan}", color = Color.Gray)
                        }
                        Divider(Modifier.padding(vertical = 8.dp))
                        Text(
                            "Subtotal: Rp ${item.subtotal}",
                            fontWeight = FontWeight.Bold,
                            color = Merah
                        )
                    }
                }
            }
        }

        Button(
            onClick = {
                viewModel.createPesanan(
                    userId = FirebaseAuth.getInstance().uid!!,
                    namaPelanggan = FirebaseAuth.getInstance().currentUser?.displayName ?: "User",
                    meja = tableNumber ?: "1",
                    items = viewModel.cartItems
                )
                navController.navigate(Screen.Success.route)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Merah)
        ) {
            Text("Pesan Sekarang")
        }
    }
}
