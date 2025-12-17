package org.d3ifcool.admin.beranda

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3ifcool.admin.R
import org.d3ifcool.admin.navigation.Screen
import org.d3ifcool.admin.ui.theme.MejaQTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BerandaAdmin(navController: NavHostController) {
    Scaffold(
        containerColor = Color(0xFFFDFDFE),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logo_mejaq),
                            contentDescription = "Logo MejaQ",
                            modifier = Modifier
                                .size(120.dp)
                                .padding(end = 6.dp)
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
        BerandaContent(
            modifier = Modifier.padding(innerPadding),
            navController = navController
        )
    }
}

@Composable
fun BerandaContent(modifier: Modifier = Modifier, navController: NavHostController) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFDFDFE))
            .padding(16.dp)
    ) {
        item {
            Text(
                text = "Selamat datang, Admin!",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = Color.Black
            )

            Text(
                text = "Berikut adalah ringkasan aktivitas Anda.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(20.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
            ) {
                SummaryCard(
                    title = "Saldo",
                    value = "Rp 12.5M",
                    backgroundColor = Color(0xFFFFE0E6)
                )
                SummaryCard(
                    title = "Event Aktif",
                    value = "3",
                    backgroundColor = Color(0xFFD4F4DD)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            GrafikPenjualan()

            Spacer(modifier = Modifier.height(20.dp))

            StatistikMenuFavorit()

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Menu Utama",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
            )

            Spacer(modifier = Modifier.height(50.dp))

            // Menu utama
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
                ) {
                    MenuButton(
                        title = "Input Menu",
                        icon = Icons.Default.Edit,
                        onClick = {navController.navigate(Screen.Menu.route)})
                    MenuButton(
                        title = "Pendapatan",
                        icon = Icons.AutoMirrored.Filled.List,
                        onClick ={navController.navigate(Screen.Keuangan.route)})
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
                ) {
                    MenuButton(
                        title = "Tambah Event",
                        icon = Icons.Default.Add,
                        onClick = { navController.navigate(Screen.Event.route) }
                    )
                    MenuButton("Logout", Icons.AutoMirrored.Filled.ExitToApp, Color(0xFFFFCDD2))
                }
            }
        }
    }
}

@Composable
fun SummaryCard(title: String, value: String, backgroundColor: Color) {
    Box(
        modifier = Modifier
            .width(160.dp)
            .height(100.dp)
            .background(backgroundColor, shape = RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxHeight()
        ) {
            Column {
                Text(
                    text = title,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Text(
                    text = value,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun MenuButton(
    title: String,
    icon: ImageVector,
    background: Color = Color(0xFFF5F5F5),
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .width(160.dp)
            .height(100.dp)
            .background(background, shape = RoundedCornerShape(12.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, contentDescription = title, tint = Color(0xFF7E57C2))
            Text(title, fontSize = 14.sp, color = Color.Black)
        }
    }
}
@Composable
fun GrafikPenjualan() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text(
            text = "Grafik Penjualan Mingguan",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(16.dp))

        // MPAndroidChart
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.Bottom
        ) {

            val salesData = listOf(0.4f, 0.7f, 0.5f, 0.6f, 0.6f, 0.3f, 0.8f)
            val labels = listOf("Sen", "Sel", "Rab", "Kam", "Jum", "Sab", "Min")

            salesData.forEachIndexed { index, fraction ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom,
                    modifier = Modifier.height(120.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .width(24.dp)
                            .fillMaxHeight(fraction)
                            .background(
                                Color(0xFF7E57C2),
                                shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                            )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(labels[index], fontSize = 10.sp, color = Color.Gray)
                }
            }
        }

    }
}

@Composable
fun StatistikMenuFavorit() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text(
            text = "Menu Favorit",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(12.dp))

        MenuItemRow(rank = 1, name = "Nasi Goreng Spesial", sold = 120)
        MenuItemRow(rank = 2, name = "Ayam Bakar Madu", sold = 98)
        MenuItemRow(rank = 3, name = "Es Teh Manis", sold = 75)
    }
}

@Composable
fun MenuItemRow(rank: Int, name: String, sold: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$rank.",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = Color.Black,
            modifier = Modifier.width(24.dp)
        )
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "$sold Terjual",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BerandaAdminPreview() {
    MejaQTheme {
        BerandaAdmin(rememberNavController())
    }
}
