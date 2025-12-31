package org.d3ifcool.mejaq.ui.pemesanan

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.d3ifcool.shared.model.Menu

@Composable
fun MenuGridItem(
    menu: Menu,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.clickable(onClick = onClick),
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
                Text("Rp ${menu.price}", color = Color(0xFFD61355))
            }
        }
    }
}


