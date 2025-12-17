package org.d3ifcool.admin.event

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3ifcool.admin.ui.theme.MejaQTheme
import org.d3ifcool.shared.R
import org.d3ifcool.shared.screen.LayoutPage
import org.d3ifcool.shared.model.Catatan

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailEventScreen(navController: NavHostController, eventId: Int) {
    val event = getEventById(eventId)

    Scaffold(
        containerColor = Color(0xFFFDFDFE),
        topBar = {
            TopAppBar(
                title = {
                    LayoutPage(navController = navController)
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0xFFFDFDFE),
                    titleContentColor = Color(0xFF6A1B9A),
                )
            )
        }
    ) { innerPadding ->
        DetailEventContent(event = event, modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun DetailEventContent(event: Catatan, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Image(
            painter = painterResource(id = event.foto),
            contentDescription = "Event Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .background(Color(0xFFF2F2F2), RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )

        Text(
            text = event.catatan,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color.Black
        )
        Text(
            text = event.dekskripsi,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Light,
            fontSize = 16.sp,
            color = Color.Black
        )
        Text(
            text = "Tanggal: ${event.tanggal}",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
    }
}

fun getEventById(eventId: Int): Catatan {
    return when (eventId) {
        1 ->  Catatan(
            id = 1,
            foto = R.drawable.images1,
            catatan = "Tech Innovation Talk\nAcara seminar tentang inovasi teknologi terkini dan tren AI di dunia industri.",
            dekskripsi = "Seminar ini bertujuan untuk memperkenalkan inovasi terbaru dalam dunia teknologi, terutama tentang kecerdasan buatan (AI). Pembicara ahli akan membahas tren AI terkini dan bagaimana teknologi ini memengaruhi industri di berbagai sektor.",
            tanggal = "2025-03-10 09:00:00"
        )
        2-> Catatan(
            id = 2,
            foto = R.drawable.images2,
            catatan = "Workshop UI/UX Design\nPelatihan intensif tentang cara membuat desain antarmuka yang menarik dan ramah pengguna.",
            dekskripsi = "Workshop ini dirancang untuk memberikan pemahaman mendalam tentang desain antarmuka pengguna (UI) dan pengalaman pengguna (UX). Peserta akan belajar cara membuat desain yang tidak hanya estetis, tetapi juga mudah digunakan oleh pengguna akhir.",
            tanggal = "2025-03-15 13:30:00"
        )
        3->Catatan(
            id = 3,
            foto = R.drawable.images3,
            catatan = "Startup Expo 2025\nPameran startup mahasiswa yang menampilkan berbagai produk digital inovatif.",
            dekskripsi = "Startup Expo 2025 adalah acara yang mengumpulkan startup mahasiswa dari berbagai universitas untuk memamerkan produk dan inovasi digital mereka. Acara ini bertujuan untuk mendukung pertumbuhan wirausaha muda dan memberikan mereka kesempatan untuk berjejaring dengan para profesional industri.",
            tanggal = "2025-03-22 10:00:00"
        )
        else -> throw IllegalArgumentException("Event not found!")
    }
}


@Preview(showBackground = true)
@Composable
fun DetailEventScreenPreview() {
    MejaQTheme {
        DetailEventScreen(navController = rememberNavController(), eventId = 1)
    }
}
