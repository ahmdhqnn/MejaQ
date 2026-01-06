package org.d3ifcool.mejaq.ui.pemesanan

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import org.d3ifcool.mejaq.navigation.Screen
import org.d3ifcool.shared.viewmodel.MenuViewModel
import org.d3ifcool.shared.viewmodel.PesananViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreen(
    navController: NavHostController,
    tableNumber: String?,
    menuViewModel: MenuViewModel = viewModel(),
    pesananViewModel: PesananViewModel
) {
    val uiState by menuViewModel.uiState.collectAsState()

    LaunchedEffect(tableNumber) {
        tableNumber?.let {
            pesananViewModel.setMeja(it)
        }
    }

    Scaffold(
        bottomBar = {
            Button(
                onClick = { navController.navigate(Screen.Cart.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD61355)
                )
            ) {
                Text("Lihat Keranjang (${pesananViewModel.cartItems.size})")
            }
        }
    ) { padding ->

        Column(
            Modifier
                .padding(padding)
                .padding(12.dp)
        ) {

            val selectedMeja by pesananViewModel.selectedMeja.collectAsState()

            OutlinedTextField(
                value = "Meja $selectedMeja",
                onValueChange = {},
                readOnly = true,
                label = { Text("Nomor Meja") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            LazyVerticalGrid(
                columns = GridCells.Adaptive(160.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.menus) { menu ->
                    MenuGridItem(
                        menu = menu,
                        onClick = {
                            navController.navigate(
                                Screen.DetailMenu.createRoute(menu.id)
                            )
                        }
                    )
                }
            }
        }
    }
}

