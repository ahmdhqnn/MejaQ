package org.d3ifcool.mejaq.ui.pemesanan

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.d3ifcool.shared.model.Menu

@Composable
fun AddToCartDialog(
    menu: Menu,
    onDismiss: () -> Unit,
    onAdd: (Int, String) -> Unit
) {
    var qty by remember { mutableStateOf(1) }
    var note by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(menu.name) },
        text = {
            Column {
                OutlinedTextField(
                    value = qty.toString(),
                    onValueChange = {
                        qty = it.toIntOrNull()?.coerceAtLeast(1) ?: 1
                    },
                    label = { Text("Jumlah") }
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Catatan") }
                )
            }
        },
        confirmButton = {
            Button(onClick = { onAdd(qty, note) }) {
                Text("Tambah")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}
