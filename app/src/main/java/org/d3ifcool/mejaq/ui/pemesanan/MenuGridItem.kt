package org.d3ifcool.mejaq.ui.pemesanan

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import org.d3ifcool.mejaq.ui.theme.MerahMuda
import org.d3ifcool.mejaq.ui.theme.MerahUtama
import org.d3ifcool.shared.model.Menu

@Composable
fun MenuGridItem(
    menu: Menu,
    onAddToCart: (Int, String) -> Unit
) {
    var qty by remember { mutableStateOf(1) }
    var note by remember { mutableStateOf("") }

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {

            AsyncImage(
                model = menu.imageUrl,
                contentDescription = menu.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentScale = ContentScale.Crop
            )

            Column(Modifier.padding(12.dp)) {

                Text(menu.name, fontWeight = FontWeight.Bold)
                Text("Rp ${menu.price}", color = MerahUtama)

                Spacer(Modifier.height(8.dp))

                // ===== QTY CONTROL =====
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    IconButton(
                        onClick = { if (qty > 1) qty-- },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MerahMuda
                        )
                    ) {
                        Text("-", color = MerahUtama, fontSize = 20.sp)
                    }

                    Text(qty.toString(), fontWeight = FontWeight.Bold)

                    IconButton(
                        onClick = { qty++ },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MerahMuda
                        )
                    ) {
                        Text("+", color = MerahUtama, fontSize = 20.sp)
                    }
                }

                Spacer(Modifier.height(6.dp))

                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Catatan") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MerahUtama,
                        focusedLabelColor = MerahUtama
                    )
                )

                Spacer(Modifier.height(8.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MerahUtama),
                    onClick = {
                        onAddToCart(qty, note)
                        qty = 1
                        note = ""
                    }
                ) {
                    Text("Tambah", color = Color.White)
                }
            }
        }
    }
}

