package org.d3ifcool.mejaq.ui.pemesanan

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import org.d3ifcool.mejaq.R
import org.d3ifcool.mejaq.navigation.Screen
import org.d3ifcool.mejaq.ui.theme.MejaqAppTheme
import org.d3ifcool.shared.model.Menu
import org.d3ifcool.shared.screen.LayoutPage

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
        DetailMenuContent(
            navController = navController,
            menu = menu,
            modifier = Modifier.padding(innerPadding)
        )

    }
}

@Composable
fun DetailMenuContent(navController: NavHostController,menu: Menu, modifier: Modifier = Modifier) {

    var quantity by remember { mutableStateOf(1) }
    var notes by remember { mutableStateOf("") }

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
            fontWeight = FontWeight.Bold
        )

        Text(
            text = menu.description,
            fontSize = 16.sp,
            color = Color.Gray
        )

        Text(
            text = "Harga: Rp ${menu.price}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
        //button plus minus
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Button(
                onClick = {
                    if (quantity > 1) quantity--
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD61355)
                )
            ) {
                Text(
                    text = "-",
                    fontSize = 20.sp,
                    color = Color.White
                )
            }


            Text(
                text = quantity.toString(),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Button(
                onClick = { quantity++ },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD61355)
                )
            ) {
                Text(
                    text = "+",
                    fontSize = 20.sp,
                    color = Color.White
                )
            }

        }

        OutlinedTextField(
            value = notes,
            onValueChange = { notes = it },
            label = { Text("Catatan (opsional)") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                navController.navigate(Screen.Cart.route)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFD61355)
            )
        ) {
            Text(
                text = "Tambah ke Keranjang",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

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
    MejaqAppTheme {
        DetailMenuScreen(rememberNavController(), menuId = 1)
    }
}
