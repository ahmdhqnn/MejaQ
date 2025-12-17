package org.d3ifcool.shared.model

data class Pesanan(
    val id: Int,
    val namaPelanggan: String,
    val meja: String,
    val tanggal: String,
    val waktu: String,
    val daftarMenu: List<ItemMenu>,
    var status: String = "Pending",  // ⬅ default status
    var isExpanded: Boolean = false
)

data class ItemMenu(
    val nama: String,
    val catatan: String,
    val jumlah: Int
)
