package org.d3ifcool.mejaq.ui.riwayat

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.d3ifcool.mejaq.R
import org.d3ifcool.mejaq.navigation.Screen

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 12.dp,
                bottom = 8.dp
            ),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconButton(
            onClick = { navController.navigate(Screen.Home.route) },
            modifier = Modifier.size(96.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.button_beranda),
                contentDescription = "Beranda",
                modifier = Modifier.fillMaxSize()
            )
        }

        IconButton(
            onClick = { navController.navigate(Screen.QrCodeScanner.route) },
            modifier = Modifier.size(72.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.button_scanner),
                contentDescription = "Scan",
                modifier = Modifier.fillMaxSize()
            )
        }

        IconButton(
            onClick = { navController.navigate(Screen.Riwayat.route) },
            modifier = Modifier.size(96.dp)
        ) {
            Image(
                painter = painterResource(
                    id = org.d3ifcool.shared.R.drawable.button_riwayat
                ),
                contentDescription = "Riwayat",
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
