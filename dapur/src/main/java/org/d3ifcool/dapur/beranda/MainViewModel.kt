package org.d3ifcool.dapur.beranda

import androidx.lifecycle.ViewModel
import org.d3ifcool.shared.model.ItemMenu
import org.d3ifcool.shared.model.Pesanan

class MainViewModel : ViewModel() {
    val data = listOf(
        Pesanan(
            id = 1,
            namaPelanggan = "Dzikri",
            meja = "Meja 1 - Dzikri",
            tanggal = "7 Agustus 2025",
            waktu = "12:45",
            daftarMenu = listOf(
                ItemMenu("Indomie Goreng", "Tidak pedas",1),
                ItemMenu("Indomie Rebus", "",1)
            )
        ),
        Pesanan(
            id = 2,
            namaPelanggan = "Ansyari",
            meja = "Meja 15 - Ansyari",
            tanggal = "7 Agustus 2025",
            waktu = "13:10",
            daftarMenu = listOf(
                ItemMenu("Indomie Goreng", "Pedas",1)
            )
        )
    )
}
