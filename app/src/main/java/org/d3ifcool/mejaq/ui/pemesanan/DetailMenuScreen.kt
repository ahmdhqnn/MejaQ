package org.d3ifcool.mejaq.ui.pemesanan

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import org.d3ifcool.mejaq.navigation.Screen
import org.d3ifcool.shared.viewmodel.MenuViewModel
import org.d3ifcool.shared.viewmodel.PesananViewModel

@Composable
fun DetailMenuScreen(
    navController: NavHostController,
    menuId: String,
    menuViewModel: MenuViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    pesananViewModel: PesananViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val menu = menuViewModel.uiState.collectAsState().value.menus
        .find { it.id == menuId }

    var qty by remember { mutableIntStateOf(1) }
    var note by remember { mutableStateOf("") }

    menu?.let { it ->
        Column(Modifier.padding(16.dp)) {

            AsyncImage(
                model = it.imageUrl,
                contentDescription = it.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                contentScale = ContentScale.Crop
            )

            Text(it.name, style = MaterialTheme.typography.titleLarge)
            Text(it.description)

            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(onClick = { if (qty > 1) qty-- }) { Text("-") }
                Text(qty.toString(), Modifier.padding(horizontal = 16.dp))
                Button(onClick = { qty++ }) { Text("+") }
            }

            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("Catatan") }
            )

            Button(
                onClick = {
                    pesananViewModel.addToCart(it, qty, note)
                    navController.navigate(Screen.Cart.route)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Tambah ke Keranjang")
            }
        }
    }
}
