package org.d3ifcool.admin.menu

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import org.d3ifcool.admin.R
import org.d3ifcool.admin.ui.theme.MejaQTheme
import org.d3ifcool.shared.viewmodel.MenuViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputMenuScreen(
    navController: NavHostController,
    menuViewModel:  MenuViewModel = viewModel()
) {
    val uiState by menuViewModel.uiState.collectAsState()
    val context = LocalContext.current

    var namaMenu by remember { mutableStateOf("") }
    var hargaMenu by remember { mutableStateOf("") }
    var deskripsiMenu by remember { mutableStateOf("") }
    var kategoriMenu by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }

    // Handle success message
    LaunchedEffect(uiState.successMessage) {
        uiState.successMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            menuViewModel.clearMessages()
            navController.popBackStack()
        }
    }

    // Handle error message
    LaunchedEffect(uiState.errorMessage) {
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBackIosNew,
                                contentDescription = stringResource(R. string.kembali),
                                tint = Color(0xFFD61355)
                            )
                        }
                        Text(
                            text = "Tambah Menu",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color. Black
                        )
                        Image(
                            painter = painterResource(id = R.drawable. logo_mejaq),
                            contentDescription = "Logo MejaQ",
                            modifier = Modifier.size(90.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults. mediumTopAppBarColors(
                    containerColor = Color(0xFFFDFDFE),
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalArrangement = Arrangement. spacedBy(16.dp)
        ) {
            // Image Preview atau Placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(Color(0xFFF2F2F2), RoundedCornerShape(12.dp))
                    .clickable { /* TODO: Image picker jika diperlukan */ },
                contentAlignment = Alignment.Center
            ) {
                if (imageUrl. isNotEmpty()) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "Preview Menu",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.AddAPhoto,
                        contentDescription = "Tambah Foto",
                        tint = Color(0xFFD61355),
                        modifier = Modifier. size(50.dp)
                    )
                }
            }

            // URL Gambar (untuk input manual)
            OutlinedTextField(
                value = imageUrl,
                onValueChange = { imageUrl = it },
                label = { Text("URL Gambar (opsional)") },
                placeholder = { Text("https://example.com/image.jpg") },
                modifier = Modifier. fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            OutlinedTextField(
                value = namaMenu,
                onValueChange = { namaMenu = it },
                label = { Text("Nama Menu") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            OutlinedTextField(
                value = hargaMenu,
                onValueChange = { hargaMenu = it. filter { char -> char.isDigit() } },
                label = { Text("Harga Menu") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            OutlinedTextField(
                value = kategoriMenu,
                onValueChange = { kategoriMenu = it },
                label = { Text("Kategori (Makanan/Minuman)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            OutlinedTextField(
                value = deskripsiMenu,
                onValueChange = { deskripsiMenu = it },
                label = { Text("Deskripsi Menu") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                shape = RoundedCornerShape(12.dp)
            )

            Button(
                onClick = {
                    if (namaMenu.isNotBlank() && hargaMenu.isNotBlank()) {
                        menuViewModel.addMenu(
                            name = namaMenu,
                            description = deskripsiMenu,
                            price = hargaMenu.toIntOrNull() ?: 0,
                            category = kategoriMenu,
                            imageUrl = imageUrl
                        )
                    } else {
                        Toast.makeText(
                            context,
                            "Nama dan harga menu wajib diisi",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD61355)),
                enabled = ! uiState.isLoading
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color. White
                    )
                } else {
                    Text(
                        text = "Simpan",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InputMenuPreview() {
    MejaQTheme {
        InputMenuScreen(rememberNavController())
    }
}