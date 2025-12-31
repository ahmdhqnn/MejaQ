package org.d3ifcool.mejaq.ui.pemesanan

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import org.d3ifcool.mejaq.navigation.Screen
import org.d3ifcool.shared.viewmodel.MenuViewModel
import org.d3ifcool.shared.viewmodel.PesananViewModel
import org.d3ifcool.mejaq.ui.theme.Merah
import org.d3ifcool.mejaq.ui.theme.Putih
import org.d3ifcool.shared.R



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailMenuScreen(
    navController: NavHostController,
    menuId: String,
    pesananViewModel: PesananViewModel,
    menuViewModel: MenuViewModel = viewModel()
) {
    val menu = menuViewModel.uiState.collectAsState().value.menus
        .find { it.id == menuId }

    var qty by remember { mutableIntStateOf(1) }
    var note by remember { mutableStateOf("") }

    menu ?: return

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
            var isAdded by remember { mutableStateOf(false) }

            Button(
                onClick = {
                    if (!isAdded) {
                        pesananViewModel.addToCart(menu, qty, note)
                        isAdded = true
                        navController.navigate(Screen.Cart.route)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(52.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Merah)
            ) {
                Text("Tambah ke Keranjang", color = Color.White)
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            AsyncImage(
                model = menu.imageUrl,
                contentDescription = menu.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.height(16.dp))

            Text(menu.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(menu.description, color = Color.Gray)

            Spacer(Modifier.height(8.dp))

            Text("Harga: Rp ${menu.price}", fontWeight = FontWeight.Medium)

            Spacer(Modifier.height(16.dp))

            // ===== QTY CONTROL =====
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {

                IconButton(
                    onClick = { if (qty > 1) qty-- },
                    modifier = Modifier
                        .size(44.dp)
                        .background(Merah, RoundedCornerShape(12.dp))
                ) {
                    Text("-", color = Color.White, fontSize = 20.sp)
                }

                Text(
                    qty.toString(),
                    fontSize = 18.sp,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                IconButton(
                    onClick = { qty++ },
                    modifier = Modifier
                        .size(44.dp)
                        .background(Merah, RoundedCornerShape(12.dp))
                ) {
                    Text("+", color = Color.White, fontSize = 20.sp)
                }
            }

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                placeholder = { Text("Catatan (opsional)") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}



