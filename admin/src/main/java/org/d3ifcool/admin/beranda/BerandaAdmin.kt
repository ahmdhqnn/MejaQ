@file:Suppress("DEPRECATION")

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font. FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3ifcool.admin.R
import org.d3ifcool.admin.navigation.Screen
import org.d3ifcool.admin.ui.theme.MejaQTheme
import org.d3ifcool.shared.viewmodel.AuthViewModel
import org.d3ifcool.shared.viewmodel.DashboardViewModel
import org.d3ifcool.shared.viewmodel.MenuViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BerandaAdmin(
    navController: NavHostController,
    authViewModel: AuthViewModel = viewModel(),
    dashboardViewModel: DashboardViewModel = viewModel(),
    menuViewModel: MenuViewModel = viewModel()
) {
    val authState by authViewModel.uiState.collectAsState()
    val dashboardState by dashboardViewModel.uiState.collectAsState()
    val menuState by menuViewModel. uiState.collectAsState()

    Scaffold(
        containerColor = Color(0xFFFDFDFE),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier. fillMaxWidth(),
                        horizontalArrangement = Arrangement. End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable. logo_mejaq),
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
        if (dashboardState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFFD61355))
            }
        } else {
            BerandaContent(
                modifier = Modifier. padding(innerPadding),
                navController = navController,
                userName = authState.userData?.name ?: "Admin",
                totalRevenue = dashboardState.totalRevenue,
                activeEventsCount = dashboardState.activeEventsCount,
                weeklyRevenue = dashboardState. weeklyRevenue,
                topMenus = menuState.topMenus,
                onLogout = {
                    authViewModel.signOut()
                    navController. navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}

@Composable
fun BerandaContent(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    userName: String,
    totalRevenue: Int,
    activeEventsCount: Int,
    weeklyRevenue: List<org.d3ifcool.shared.viewmodel.DailyRevenue>,
    topMenus: List<org.d3ifcool.shared.model.Menu>,
    onLogout: () -> Unit
) {
    val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFDFDFE))
            .padding(16.dp)
    ) {
        item {
            Text(
                text = "Selamat datang, $userName!",
                modifier = Modifier.testTag("admin_home_title"),
                style = MaterialTheme.typography.titleLarge. copy(fontWeight = FontWeight.Bold),
                color = Color. Black
            )

            Text(
                text = "Berikut adalah ringkasan aktivitas Anda.",
                style = MaterialTheme. typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier. fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
            ) {
                SummaryCard(
                    title = "Saldo",
                    value = formatter.format(totalRevenue).replace("Rp", "Rp "),
                    backgroundColor = Color(0xFFFFE0E6)
                )
                SummaryCard(
                    title = "Event Aktif",
                    value = activeEventsCount.toString(),
                    backgroundColor = Color(0xFFD4F4DD)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            GrafikPenjualan(weeklyRevenue = weeklyRevenue)

            Spacer(modifier = Modifier.height(20.dp))

            StatistikMenuFavorit(topMenus = topMenus)

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Menu Utama",
                style = MaterialTheme.typography.titleMedium. copy(fontWeight = FontWeight. SemiBold)
            )

            Spacer(modifier = Modifier.height(50.dp))

            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
                ) {
                    MenuButton(
                        title = "Input Menu",
                        icon = Icons. Default.Edit,
                        onClick = { navController.navigate(Screen.Menu.route) }
                    )
                    MenuButton(
                        title = "Pendapatan",
                        icon = Icons.AutoMirrored. Filled.List,
                        onClick = { navController.navigate(Screen.Keuangan.route) }
                    )
                }
                Spacer(modifier = Modifier. height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
                ) {
                    MenuButton(
                        title = "Tambah Event",
                        icon = Icons. Default.Add,
                        onClick = { navController.navigate(Screen.Event.route) }
                    )
                    MenuButton(
                        title = "Logout",
                        icon = Icons. AutoMirrored.Filled. ExitToApp,
                        background = Color(0xFFFFCDD2),
                        onClick = onLogout
                    )
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
            verticalArrangement = Arrangement. SpaceBetween,
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
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    maxLines = 1
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
        contentAlignment = Alignment. Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, contentDescription = title, tint = Color(0xFF7E57C2))
            Text(title, fontSize = 14.sp, color = Color.Black)
        }
    }
}

@Composable
fun GrafikPenjualan(
    weeklyRevenue: List<org.d3ifcool.shared.viewmodel.DailyRevenue>
) {
    val chartHeight = 100.dp

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

        val maxRevenue = weeklyRevenue.maxOfOrNull { it.revenue }?.takeIf { it > 0 } ?: 1

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.Bottom
        ) {

            val data = weeklyRevenue.ifEmpty {
                listOf("Sen", "Sel", "Rab", "Kam", "Jum", "Sab", "Min").map {
                    org.d3ifcool.shared.viewmodel.DailyRevenue(it, 0)
                }
            }

            data.forEach { daily ->

                val barHeight =
                    (daily.revenue.toFloat() / maxRevenue)
                        .coerceIn(0.05f, 1f)

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {

                    Box(
                        modifier = Modifier
                            .height(chartHeight)
                            .width(24.dp),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(chartHeight * barHeight)
                                .background(
                                    Color(0xFF7E57C2),
                                    shape = RoundedCornerShape(
                                        topStart = 6.dp,
                                        topEnd = 6.dp
                                    )
                                )
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = daily.dayName,
                        fontSize = 10.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}



@Composable
fun StatistikMenuFavorit(
    topMenus: List<org.d3ifcool.shared.model.Menu>
) {

    val filteredMenus = topMenus
        .filter { it.soldCount > 0 }
        .sortedByDescending { it.soldCount }
        .take(3)

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

        if (filteredMenus.isEmpty()) {
            Text(
                text = "Belum ada menu terjual",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        } else {
            filteredMenus.forEachIndexed { index, menu ->
                MenuItemRow(
                    rank = index + 1,
                    name = menu.name,
                    sold = menu.soldCount
                )
            }
        }
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
            style = MaterialTheme. typography.bodyMedium. copy(fontWeight = FontWeight. Bold),
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
            color = Color. Gray
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