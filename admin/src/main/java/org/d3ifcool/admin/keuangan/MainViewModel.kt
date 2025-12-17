package org.d3ifcool.admin.keuangan

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
        ),
        Transaksi(
            id = 2,
            namaPelanggan = "Dzikri",
            meja = "Meja 1",
            tanggal = "7 Agustus 2025",
            waktu = "12:45",
            nama = "Es Teh Manis",
            jumlah = 1,
            total = 1 * 5000
        ),
        Transaksi(
            id = 3,
            namaPelanggan = "Ansyari",
            meja = "Meja 15",
            tanggal = "7 Agustus 2025",
            waktu = "13:10",
            nama = "Indomie Rebus",
            jumlah = 1,
            total = 1 * 15000
        ),
        Transaksi(
            id = 4,
            namaPelanggan = "Ansyari",
            meja = "Meja 15",
            tanggal = "7 Agustus 2025",
            waktu = "13:10",
            nama = "Es Jeruk",
            jumlah = 2,
            total = 2 * 8000
        ),
        Transaksi(
            id = 5,
            namaPelanggan = "Bima",
            meja = "Meja 7",
            tanggal = "7 Agustus 2025",
            waktu = "14:20",
            nama = "Mie Ayam Bakso",
            jumlah = 1,
            total = 1 * 20000
        )
    )
}
