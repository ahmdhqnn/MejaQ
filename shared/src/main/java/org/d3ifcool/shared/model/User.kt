package org.d3ifcool.shared.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

data class User(
    @DocumentId
    val id: String = "",
    val email: String = "",
    val name: String = "",
    val role: String = "user",
    val photoUrl: String = "",
    @ServerTimestamp
    val createdAt: Timestamp? = null  // Ubah dari Date ke Timestamp
) {
    constructor() : this(
        id = "",
        email = "",
        name = "",
        role = "user",
        photoUrl = "",
        createdAt = null
    )
}