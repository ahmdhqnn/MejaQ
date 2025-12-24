package org.d3ifcool.shared.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

data class Event(
    @DocumentId
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val eventDate: String = "",
    val eventTime:  String = "",
    val location: String = "",
    val isActive: Boolean = true,
    @ServerTimestamp
    val createdAt: Timestamp? = null
) {
    constructor() : this(
        id = "",
        title = "",
        description = "",
        imageUrl = "",
        eventDate = "",
        eventTime = "",
        location = "",
        isActive = true,
        createdAt = null
    )
}