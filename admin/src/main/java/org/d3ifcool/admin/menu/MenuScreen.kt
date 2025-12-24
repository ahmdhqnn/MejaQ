package org.d3ifcool.admin.menu

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose. material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
import org.d3ifcool.shared.model.Menu
import org.d3ifcool.shared.screen.LayoutPage
import org.d3ifcool.shared.viewmodel.MenuViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    navController: NavHostController,
    menuViewModel: MenuViewModel = viewModel()
) {
    val uiState by menuViewModel.uiState.collectAsState()

    Scaffold(
        containerColor = Color(0xFFFDFDFE),
        topBar = {
            TopAppBar(
                title = {
                    LayoutPage(navController = navController)
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0xFFFDFDFE),
                    titleContentColor = Color(0xFFD61355),
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.InputMenu.route) },
                containerColor = Color(0xFFD61355),
                shape = CircleShape,
                modifier = Modifier. size(64.dp)
            ) {
                Icon(
                    imageVector = Icons. Filled.Add,
                    contentDescription = stringResource(R.string.tambah_menu),
                    tint = Color. White,
                    modifier = Modifier. size(32.dp)
                )
            }
        }
    ) { innerPadding ->
        if (uiState. isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFFD61355))
            }
        } else if (uiState.menus. isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment. Center
            ) {
                Text(
                    text = "Belum ada menu.\nTambahkan menu pertama Anda! ",
                    color = Color. Gray,
                    modifier = Modifier.padding(16.dp)
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells. Adaptive(180.dp),
                contentPadding = innerPadding,
                verticalArrangement = Arrangement. spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(8.dp)
            ) {
                items(uiState.menus) { menu ->
                    MenuGridItem(
                        menu = menu,
                        onClick = {
                            navController.navigate(Screen.DetailMenu.createRoute(menu.id))
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MenuGridItem(
    menu: Menu,
    onClick: () -> Unit
) {
    val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, DividerDefaults.color),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Image dari URL atau placeholder
            if (menu.imageUrl.isNotEmpty()) {
                AsyncImage(
                    model = menu.imageUrl,
                    contentDescription = menu.name,
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                // Placeholder jika tidak ada gambar
                Box(
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🍽️",
                        fontSize = 40.sp
                    )
                }
            }

            Text(
                text = menu.name,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                maxLines = 1,
                modifier = Modifier.padding(top = 8.dp)
            )

            Text(
                text = menu.description,
                fontSize = 10.sp,
                color = Color.Gray,
                maxLines = 1
            )

            Row(
                modifier = Modifier
                    . fillMaxWidth()
                    . padding(top = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatter.format(menu. price),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = Color(0xFFD61355)
                )

                // Status ketersediaan
                if (! menu.isAvailable) {
                    Text(
                        text = "Habis",
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
fun MenuPreview() {
    MejaQTheme {
        MenuScreen(rememberNavController())
    }
}