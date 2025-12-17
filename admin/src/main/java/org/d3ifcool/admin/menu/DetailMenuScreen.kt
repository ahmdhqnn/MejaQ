package org.d3ifcool.admin.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import org.d3ifcool.admin.R
import org.d3ifcool.admin.ui.theme.MejaQTheme
import org.d3ifcool.shared.screen.LayoutPage
import org.d3ifcool.shared.model.Menu

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailMenuScreen(navController: NavHostController, menuId: Int) {
    val menu = getMenuById(menuId)

    Scaffold(
        containerColor = Color(0xFFFDFDFE),
        topBar = {
            TopAppBar(
                title = {
                    LayoutPage(navController = navController)
                },
            )
        }
    ) { innerPadding ->
        DetailMenuContent(menu = menu, modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun DetailMenuContent(menu: Menu, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Image(
            painter = painterResource(id = menu.imageUrl),
            contentDescription = "Menu Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .background(Color(0xFFF2F2F2), RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )

        Text(
            text = menu.name,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Text(
            text = menu.description,
            fontSize = 16.sp,
            fontWeight = FontWeight.Light,
            color = Color.Black
        )
        Text(
            text = "Harga: Rp ${menu.price}",
            fontSize = 16.sp,
            color = Color.Gray
        )
    }
}

// Dummy data untuk preview
fun getMenuById(menuId: Int): Menu {
    return when (menuId) {
        1 -> Menu(1, "Indomie Goreng", "Indomie goreng + telur", 10000, 0, R.drawable.mie_goreng)
        2 -> Menu(2, "Indomie Rebus", "Indomie rebus + telur", 10000, 0, R.drawable.mie_rebus)
        3-> Menu(3, "Nasi Goreng", "Nasi goreng spesial", 15000, 0, R.drawable.nasi_goreng)
        4-> Menu(4, "Magelangan", "Nasi + Mie goreng", 18000, 0, R.drawable.magelangan)
        5->Menu(5, "Es Teh Manis", "Teh manis dingin", 5000, 0, R.drawable.es_teh)
        else -> throw IllegalArgumentException("Menu not found!")
    }
}

@Preview(showBackground = true)
@Composable
fun DetailMenuScreenPreview() {
    MejaQTheme {
        DetailMenuScreen(navController = rememberNavController(), menuId = 1)
    }
}
