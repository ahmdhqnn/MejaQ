package org.d3ifcool.admin.event

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape. RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import org.d3ifcool.shared.model.Event
import org.d3ifcool.shared.screen.LayoutPage
import org.d3ifcool.shared.viewmodel.EventViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailEventScreen(
    navController: NavHostController,
    eventId: String,
    eventViewModel:  EventViewModel = viewModel()
) {
    val uiState by eventViewModel.uiState.collectAsState()
    val context = LocalContext.current
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Find event by ID from the list
    val event = uiState.events.find { it. id == eventId }

    // Handle messages
    LaunchedEffect(uiState.successMessage) {
        uiState.successMessage?.let { message ->
            Toast.makeText(context, message, Toast. LENGTH_SHORT).show()
            eventViewModel.clearMessages()
            if (message.contains("dihapus")) {
                navController.popBackStack()
            }
        }
    }

    LaunchedEffect(uiState. errorMessage) {
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
                    LayoutPage(navController = navController)
                },
                actions = {
                    if (event != null) {
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(
                                imageVector = Icons.Default. Delete,
                                contentDescription = "Hapus Event",
                                tint = Color. Red
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0xFFFDFDFE),
                    titleContentColor = Color(0xFF6A1B9A),
                )
            )
        }
    ) { innerPadding ->
        if (event == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment. Center
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(color = Color(0xFFD61355))
                } else {
                    Text(
                        text = "Event tidak ditemukan",
                        color = Color. Gray
                    )
                }
            }
        } else {
            DetailEventContent(
                event = event,
                modifier = Modifier. padding(innerPadding),
                onToggleStatus = {
                    eventViewModel.toggleEventStatus(event)
                },
                isLoading = uiState.isLoading
            )
        }
    }

    // Delete Confirmation Dialog
    if (showDeleteDialog && event != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Hapus Event") },
            text = { Text("Apakah Anda yakin ingin menghapus event \"${event. title}\"?") },
            confirmButton = {
                Button(
                    onClick = {
                        eventViewModel.deleteEvent(event)
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
fun DetailEventContent(
    event: Event,
    modifier:  Modifier = Modifier,
    onToggleStatus: () -> Unit,
    isLoading: Boolean
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalArrangement = Arrangement. spacedBy(16.dp)
    ) {
        // Image
        if (event.imageUrl.isNotEmpty()) {
            AsyncImage(
                model = event.imageUrl,
                contentDescription = "Event Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(Color(0xFFF2F2F2), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "📅",
                    fontSize = 80.sp
                )
            }
        }

        Text(
            text = event.title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color. Black
        )

        Text(
            text = event.description. ifEmpty { "Tidak ada deskripsi" },
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Light,
            color = Color.Black
        )

        Divider()

        // Info Section
        Column(verticalArrangement = Arrangement. spacedBy(8.dp)) {
            Row {
                Text(
                    text = "📅 Tanggal:  ",
                    fontWeight = FontWeight.Medium,
                    color = Color. Gray
                )
                Text(
                    text = event.eventDate. ifEmpty { "-" },
                    color = Color.Black
                )
            }

            Row {
                Text(
                    text = "🕐 Waktu: ",
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray
                )
                Text(
                    text = event.eventTime.ifEmpty { "-" },
                    color = Color.Black
                )
            }

            Row {
                Text(
                    text = "📍 Lokasi: ",
                    fontWeight = FontWeight.Medium,
                    color = Color. Gray
                )
                Text(
                    text = event.location.ifEmpty { "-" },
                    color = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Toggle Status
        Row(
            modifier = Modifier. fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Status Event",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = if (event.isActive) "Aktif" else "Tidak Aktif",
                    fontSize = 14.sp,
                    color = if (event.isActive) Color(0xFF4CAF50) else Color.Red
                )
                Spacer(modifier = Modifier.width(8.dp))
                Switch(
                    checked = event.isActive,
                    onCheckedChange = { onToggleStatus() },
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
fun DetailEventScreenPreview() {
    MejaQTheme {
        DetailEventScreen(navController = rememberNavController(), eventId = "1")
    }
}