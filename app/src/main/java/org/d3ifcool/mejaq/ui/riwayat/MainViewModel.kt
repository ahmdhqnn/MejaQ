package org.d3ifcool.mejaq.ui.riwayat

import androidx.lifecycle.ViewModel
import org.d3ifcool.shared.model.Transaksi

class MainViewModel : ViewModel() {

    val data = listOf(
        Transaksi(
            id = 1,
            namaPelanggan = "Dzikri",
            meja = "Meja 1",
            tanggal = "7 Agustus 2025",
            waktu = "12:45",
            nama = "Indomie Goreng",
            jumlah = 2,
            total = 2 * 15000
        )
    )
}
