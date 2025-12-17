package org.d3ifcool.shared.model

data class Transaksi(
    val id: Int,
    val namaPelanggan: String,
    val meja: String,
    val tanggal: String,
    val waktu: String,
    val nama: String,
    val jumlah: Int,
    val total: Int

)
