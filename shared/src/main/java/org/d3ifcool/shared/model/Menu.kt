package org.d3ifcool.shared.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

data class Menu(
    @DocumentId
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val price: Int = 0,
    val quantity: Int = 0,
    val imageUrl: String = "",
    val category: String = "",

    // 🔥 FIX NAMA FIELD (HARUS SAMA DENGAN FIRESTORE)
    val available: Boolean = true,

    val soldCount: Int = 0,
    @ServerTimestamp
    val createdAt: Timestamp? = null
) {
    constructor() : this(
        "", "", "", 0, 0, "", "", true, 0, null
    )
}
