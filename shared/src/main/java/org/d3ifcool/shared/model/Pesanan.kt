package org.d3ifcool.shared.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

data class Pesanan(
    @DocumentId
    val id:  String = "",
    val userId:  String = "",
    val namaPelanggan: String = "",
    val meja: String = "",
    val tanggal: String = "",
    val waktu: String = "",
    val daftarMenu: List<ItemMenu> = emptyList(),
    val totalHarga: Int = 0,
    val status: String = "Pending",
    @ServerTimestamp
    val createdAt:  Timestamp? = null,  // Ubah dari Date ke Timestamp
    val updatedAt: Timestamp? = null   // Ubah dari Long ke Timestamp
) {
    constructor() : this(
        id = "",
        userId = "",
        namaPelanggan = "",
        meja = "",
        tanggal = "",
        waktu = "",
        daftarMenu = emptyList(),
        totalHarga = 0,
        status = "Pending",
        createdAt = null,
        updatedAt = null
    )
}

data class ItemMenu(
    val menuId: String = "",
    val nama: String = "",
    val imageUrl: String = "",
    val catatan: String = "",
    val jumlah: Int = 0,
    val harga: Int = 0,
    val subtotal: Int = 0
) {
    constructor() : this("", "", "", "", 0, 0, 0)
}