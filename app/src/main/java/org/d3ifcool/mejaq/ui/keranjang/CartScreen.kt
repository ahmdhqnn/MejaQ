package org.d3ifcool.mejaq.ui.keranjang


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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3ifcool.mejaq.navigation.Screen
import org.d3ifcool.mejaq.ui.theme.MejaqAppTheme
import org.d3ifcool.shared.model.CartItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavHostController,
    viewModel: MainViewModel = viewModel()
) {
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = stringResource(
                            id = org.d3ifcool.shared.R.string.kembali
                        ),
                        tint = Color(0xFFD61355)
                    )
                }

                Image(
                    painter = painterResource(
                        id = org.d3ifcool.shared.R.drawable.logo_mejaq
                    ),
                    contentDescription = "Logo MejaQ",
                    modifier = Modifier.size(80.dp)
                )
            }
        }
    ) { innerPadding ->
        CartContent(
            modifier = Modifier.padding(innerPadding),
            viewModel = viewModel,
            navController = navController
        )

    }
}

@Composable
fun CartContent(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    navController: NavHostController
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(viewModel.cartItems) { item ->
                CartItemCard(item)
            }
        }

        PaymentSummary(
            navController = navController,
            subtotal = viewModel.subtotal(),
            serviceFee = viewModel.serviceFee(),
            total = viewModel.totalPayment()
        )
    }
}


@Composable
fun CartItemCard(item: CartItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = item.image),
                contentDescription = item.name,
                modifier = Modifier.size(64.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = item.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Rp ${item.price}",
                    fontSize = 14.sp
                )
                Text(
                    text = "Jumlah: ${item.quantity}",
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun PaymentSummary(
    navController: NavHostController,
    subtotal: Int,
    serviceFee: Int,
    total: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
        PaymentRow("Subtotal", subtotal)
        PaymentRow("Biaya Layanan", serviceFee)

        Spacer(modifier = Modifier.height(8.dp))
        androidx.compose.material3.Divider()
        Spacer(modifier = Modifier.height(8.dp))

        PaymentRow(
            title = "Total Pembayaran",
            amount = total,
            isTotal = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // nanti arahkan ke success / kasir
                navController.navigate(Screen.Success.route)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFD61355)
            )
        ) {
            Text(
                text = "Pesan Sekarang",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
fun PaymentRow(
    title: String,
    amount: Int,
    isTotal: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            fontSize = if (isTotal) 16.sp else 14.sp,
            fontWeight = if (isTotal) FontWeight.Bold else FontWeight.Normal
        )
        Text(
            text = "Rp $amount",
            fontSize = if (isTotal) 16.sp else 14.sp,
            fontWeight = if (isTotal) FontWeight.Bold else FontWeight.Normal,
            color = if (isTotal) Color(0xFFD61355) else Color.Black
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DetailMenuScreenPreview() {
    MejaqAppTheme {
        CartScreen(rememberNavController())
    }
}
