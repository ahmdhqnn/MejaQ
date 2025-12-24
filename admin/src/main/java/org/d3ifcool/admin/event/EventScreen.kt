package org.d3ifcool.admin.event

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import org.d3ifcool.admin.R
import org.d3ifcool.admin.navigation.Screen
import org.d3ifcool.admin.ui.theme.MejaQTheme
import org. d3ifcool.shared.model.Event
import org.d3ifcool.shared.screen.LayoutPage
import org. d3ifcool.shared.viewmodel.EventViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventScreen(
    navController: NavHostController,
    eventViewModel: EventViewModel = viewModel()
) {
    val uiState by eventViewModel.uiState.collectAsState()

    // Load all events when screen opens
    LaunchedEffect(Unit) {
        eventViewModel.loadAllEvents()
    }

    Scaffold(
        containerColor = Color(0xFFFDFDFE),
        topBar = {
            TopAppBar(
                title = {
                    LayoutPage(navController = navController)
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0xFFFDFDFE),
                    titleContentColor = Color(0xFFD61355)
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController. navigate(Screen.InputEvent.route) },
                containerColor = Color(0xFFD61355),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.size(64.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.tambah_event),
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    ) { innerPadding ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    . fillMaxSize()
                    . padding(innerPadding),
                contentAlignment = Alignment. Center
            ) {
                CircularProgressIndicator(color = Color(0xFFD61355))
            }
        } else if (uiState.events.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Belum ada event.\nTambahkan event pertama Anda! ",
                    color = Color. Gray,
                    modifier = Modifier.padding(16.dp)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(12.dp),
                verticalArrangement = Arrangement. spacedBy(12.dp)
            ) {
                items(uiState.events) { event ->
                    EventItemCard(
                        event = event,
                        onClick = {
                            navController.navigate(Screen.DetailEvent.createRoute(event.id))
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun EventItemCard(
    event: Event,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier. fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color. White
        ),
        border = BorderStroke(1.dp, DividerDefaults.color),
        elevation = CardDefaults. cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier. padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Image dari URL atau placeholder
            if (event.imageUrl.isNotEmpty()) {
                AsyncImage(
                    model = event.imageUrl,
                    contentDescription = event.title,
                    modifier = Modifier
                        .size(96.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "📅",
                        fontSize = 40.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.bodyMedium. copy(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier. height(4.dp))

                Text(
                    text = event.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "${event.eventDate} ${event.eventTime}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFFD61355),
                        fontWeight = FontWeight.Medium
                    )
                )

                // Status badge
                if (! event.isActive) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Tidak Aktif",
                        fontSize = 10.sp,
                        color = Color.Red
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EventPreview() {
    MejaQTheme {
        EventScreen(rememberNavController())
    }
}