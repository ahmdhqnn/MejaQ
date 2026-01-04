package org.d3ifcool.admin.event

import android.widget.Toast
import androidx.compose.foundation. Image
import androidx.compose.foundation. background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons. filled.AddAPhoto
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui. graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui. res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui. unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import org.d3ifcool. admin.R
import org. d3ifcool.admin.ui.theme.MejaQTheme
import org.d3ifcool.shared.viewmodel.EventViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputEventScreen(
    navController: NavHostController,
    eventViewModel: EventViewModel = viewModel()
) {
    val uiState by eventViewModel. uiState.collectAsState()
    val context = LocalContext. current

    var namaEvent by remember { mutableStateOf("") }
    var tanggalEvent by remember { mutableStateOf("") }
    var waktuEvent by remember { mutableStateOf("") }
    var lokasiEvent by remember { mutableStateOf("") }
    var deskripsiEvent by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }

    LaunchedEffect(uiState. successMessage) {
        uiState.successMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            eventViewModel.clearMessages()
            navController.popBackStack()
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            eventViewModel.clearMessages()
        }
    }

    Scaffold(
        containerColor = Color(0xFFFDFDFE),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement. SpaceBetween,
                        verticalAlignment = Alignment. CenterVertically
                    ) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBackIosNew,
                                contentDescription = stringResource(R.string.kembali),
                                tint = Color(0xFFD61355)
                            )
                        }
                        Text(
                            text = "Tambah Event",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color. Black
                        )
                        Image(
                            painter = painterResource(id = R.drawable.logo_mejaq),
                            contentDescription = "Logo MejaQ",
                            modifier = Modifier
                                .size(100.dp)
                                .padding(end = 4.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0xFFFDFDFE),
                    titleContentColor = Color(0xFF6A1B9A),
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(Color(0xFFF2F2F2), RoundedCornerShape(12.dp))
                    .clickable { /* TODO: Image picker */ },
                contentAlignment = Alignment.Center
            ) {
                if (imageUrl.isNotEmpty()) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "Preview Event",
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
                value = namaEvent,
                onValueChange = { namaEvent = it },
                label = { Text("Nama Event") },
                modifier = Modifier. fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            OutlinedTextField(
                value = tanggalEvent,
                onValueChange = { tanggalEvent = it },
                label = { Text("Tanggal Event") },
                placeholder = { Text("contoh: 2025-03-15") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            OutlinedTextField(
                value = waktuEvent,
                onValueChange = { waktuEvent = it },
                label = { Text("Waktu Event") },
                placeholder = { Text("contoh: 09:00") },
                modifier = Modifier. fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            OutlinedTextField(
                value = lokasiEvent,
                onValueChange = { lokasiEvent = it },
                label = { Text("Lokasi Event") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            OutlinedTextField(
                value = deskripsiEvent,
                onValueChange = { deskripsiEvent = it },
                label = { Text("Deskripsi Event") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                shape = RoundedCornerShape(12.dp)
            )

            Button(
                onClick = {
                    if (namaEvent.isNotBlank() && tanggalEvent.isNotBlank()) {
                        eventViewModel.addEvent(
                            title = namaEvent,
                            description = deskripsiEvent,
                            eventDate = tanggalEvent,
                            eventTime = waktuEvent,
                            location = lokasiEvent,
                            imageUrl = imageUrl
                        )
                    } else {
                        Toast. makeText(
                            context,
                            "Nama dan tanggal event wajib diisi",
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
                        fontWeight = FontWeight. Bold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InputEventScreenPreview() {
    MejaQTheme {
        InputEventScreen(rememberNavController())
    }
}