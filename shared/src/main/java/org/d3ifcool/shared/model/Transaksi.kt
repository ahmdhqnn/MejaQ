package org.d3ifcool.shared.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

data class Transaksi(
    @DocumentId
    val id:  String = "",
    val pesananId: String = "",
    val userId: String = "",
    val namaPelanggan: String = "",
    val meja: String = "",
    val tanggal: String = "",
    val waktu: String = "",
    val items: List<ItemMenu> = emptyList(),
    val total: Int = 0,
    val metodePembayaran: String = "",
    val status: String = "Lunas",
    @ServerTimestamp
    val createdAt:  Timestamp? = null
) {
    constructor() : this(
        id = "",
        pesananId = "",
        userId = "",
        namaPelanggan = "",
        meja = "",
        tanggal = "",
        waktu = "",
        items = emptyList(),
        total = 0,
        metodePembayaran = "",
        status = "Lunas",
        createdAt = null
    )
}