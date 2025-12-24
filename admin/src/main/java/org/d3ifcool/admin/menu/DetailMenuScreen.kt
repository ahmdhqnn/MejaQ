package org.d3ifcool.admin.menu

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import org.d3ifcool.admin.ui.theme.MejaQTheme
import org.d3ifcool.shared.model.Menu
import org.d3ifcool.shared.screen.LayoutPage
import org.d3ifcool.shared.viewmodel.MenuViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailMenuScreen(
    navController: NavHostController,
    menuId: String,
    menuViewModel: MenuViewModel = viewModel()
) {
    val uiState by menuViewModel.uiState.collectAsState()
    val context = LocalContext.current
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Find menu by ID from the list
    val menu = uiState.menus.find { it.id == menuId }

    // Handle messages
    LaunchedEffect(uiState.successMessage) {
        uiState.successMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            menuViewModel. clearMessages()
            if (message.contains("dihapus")) {
                navController.popBackStack()
            }
        }
    }

    LaunchedEffect(uiState. errorMessage) {
        uiState.errorMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            menuViewModel.clearMessages()
        }
    }

    Scaffold(
        containerColor = Color(0xFFFDFDFE),
        topBar = {
            TopAppBar(
                title = {
                    LayoutPage(navController = navController)
                },
                actions = {
                    if (menu != null) {
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Hapus Menu",
                                tint = Color. Red
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        if (menu == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(color = Color(0xFFD61355))
                } else {
                    Text(
                        text = "Menu tidak ditemukan",
                        color = Color. Gray
                    )
                }
            }
        } else {
            DetailMenuContent(
                menu = menu,
                modifier = Modifier.padding(innerPadding),
                onToggleAvailability = {
                    menuViewModel.toggleMenuAvailability(menu)
                },
                isLoading = uiState.isLoading
            )
        }
    }

    // Delete Confirmation Dialog
    if (showDeleteDialog && menu != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Hapus Menu") },
            text = { Text("Apakah Anda yakin ingin menghapus menu \"${menu.name}\"?") },
            confirmButton = {
                Button(
                    onClick = {
                        menuViewModel.deleteMenu(menu)
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color. Red)
                ) {
                    Text("Hapus", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }
}

@Composable
fun DetailMenuContent(
    menu: Menu,
    modifier: Modifier = Modifier,
    onToggleAvailability: () -> Unit,
    isLoading: Boolean
) {
    val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Image
        if (menu.imageUrl.isNotEmpty()) {
            AsyncImage(
                model = menu.imageUrl,
                contentDescription = "Menu Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .background(Color(0xFFF2F2F2), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🍽️",
                    fontSize = 80.sp
                )
            }
        }

        Text(
            text = menu.name,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        if (menu.category.isNotEmpty()) {
            Text(
                text = "Kategori: ${menu.category}",
                fontSize = 14.sp,
                color = Color(0xFFD61355)
            )
        }

        Text(
            text = menu.description. ifEmpty { "Tidak ada deskripsi" },
            fontSize = 16.sp,
            fontWeight = FontWeight.Light,
            color = Color.Black
        )

        Text(
            text = "Harga: ${formatter.format(menu.price)}",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFD61355)
        )

        Text(
            text = "Terjual: ${menu.soldCount} porsi",
            fontSize = 14.sp,
            color = Color. Gray
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Toggle Availability
        Row(
            modifier = Modifier. fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Ketersediaan",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = if (menu.isAvailable) "Tersedia" else "Habis",
                    fontSize = 14.sp,
                    color = if (menu.isAvailable) Color(0xFF4CAF50) else Color.Red
                )
                Switch(
                    checked = menu.isAvailable,
                    onCheckedChange = { onToggleAvailability() },
                    enabled = !isLoading,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color. White,
                        checkedTrackColor = Color(0xFF4CAF50),
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Color.Red
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailMenuScreenPreview() {
    MejaQTheme {
        DetailMenuScreen(navController = rememberNavController(), menuId = "1")
    }
}