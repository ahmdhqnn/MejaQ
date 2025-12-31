package org.d3ifcool.mejaq.ui.keranjang

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import org.d3ifcool.mejaq.navigation.Screen
import org.d3ifcool.mejaq.ui.theme.Merah
import org.d3ifcool.mejaq.ui.theme.Putih
import org.d3ifcool.shared.viewmodel.PesananViewModel
import org.d3ifcool.mejaq.R
import org.d3ifcool.mejaq.ui.theme.AbuCard


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavHostController,
    viewModel: PesananViewModel
) {
    val pajak = 2000
    val subtotal = viewModel.cartItems.sumOf { it.subtotal }
    val total = subtotal + pajak

    var showEditDialog by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(-1) }

    Scaffold(
        containerColor = Putih,
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBackIosNew,
                            contentDescription = "Kembali",
                            tint = Merah
                        )
                    }
                },
                title = {
                    Row(
                        Modifier.fillMaxWidth(),
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
                    containerColor = Putih
                )
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    viewModel.createPesanan(
                        userId = FirebaseAuth.getInstance().uid!!,
                        namaPelanggan = FirebaseAuth.getInstance()
                            .currentUser?.displayName ?: "User",
                        items = viewModel.cartItems // 🔥 INI WAJIB
                    )
                    navController.navigate(Screen.Success.route)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(52.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Merah)
            ) {
                Text("Pesan Sekarang", color = Color.White)
            }
        }
    ) { padding ->

        Column(
            Modifier
                .padding(padding)
                .padding(horizontal = 12.dp)
        ) {


            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {



                itemsIndexed(viewModel.cartItems) { index, item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedIndex = index
                                showEditDialog = true
                            },
                        colors = CardDefaults.cardColors(containerColor = AbuCard),
                        shape = RoundedCornerShape(12.dp)
                    ) {

                    Row(
                            Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            AsyncImage(
                                model = item.imageUrl,
                                contentDescription = item.nama,
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )


                            Spacer(Modifier.width(12.dp))

                            Column(Modifier.weight(1f)) {
                                Text(item.nama, fontWeight = FontWeight.Bold)
                                Text("Rp ${item.harga}", fontSize = 12.sp)
                                Text("Jumlah: ${item.jumlah}", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            if (showEditDialog && selectedIndex != -1) {
                val item = viewModel.cartItems[selectedIndex]

                EditCartItemDialog(
                    item = item,
                    onDismiss = {
                        showEditDialog = false
                        selectedIndex = -1
                    },
                    onSave = { qty, note ->
                        viewModel.updateCartItem(selectedIndex, qty, note)
                        showEditDialog = false
                        selectedIndex = -1
                    }
                )
            }


            HorizontalDivider(
                Modifier.padding(vertical = 12.dp),
                DividerDefaults.Thickness,
                DividerDefaults.color
            )

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Subtotal")
                Text("Rp $subtotal")
            }

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Biaya Layanan")
                Text("Rp $pajak")
            }

            Spacer(Modifier.height(8.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total Pembayaran", fontWeight = FontWeight.Bold)
                Text(
                    "Rp $total",
                    fontWeight = FontWeight.Bold,
                    color = Merah
                )
            }

            Spacer(Modifier.height(80.dp))
        }
    }
}

@Composable
fun EditCartItemDialog(
    item: org.d3ifcool.shared.model.ItemMenu,
    onDismiss: () -> Unit,
    onSave: (Int, String) -> Unit
) {
    var qty by remember { mutableIntStateOf(item.jumlah) }
    var note by remember { mutableStateOf(item.catatan) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(item.nama, fontWeight = FontWeight.Bold) },
        text = {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = { if (qty > 1) qty-- }) {
                        Text("-", fontSize = 18.sp)
                    }
                    Text(qty.toString(), Modifier.padding(horizontal = 16.dp))
                    IconButton(onClick = { qty++ }) {
                        Text("+", fontSize = 18.sp)
                    }
                }

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Catatan") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = { onSave(qty, note) }) {
                Text("Simpan")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}



