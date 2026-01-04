package org.d3ifcool.shared.repository

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import org.d3ifcool.shared.model.*
import java.util.Date

class FirestoreRepository {
    private val db: FirebaseFirestore = Firebase.firestore

    private val menuCollection = db.collection("menus")
    private val pesananCollection = db.collection("pesanan")
    private val transaksiCollection = db.collection("transaksi")
    private val eventCollection = db.collection("events")
    private val userCollection = db.collection("users")

    // ==================== MENU OPERATIONS ====================

    fun getMenusFlow(): Flow<List<Menu>> = callbackFlow {
        val listener = menuCollection
            .orderBy("name", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val menus = snapshot?.documents?.mapNotNull { doc ->
                    try {
                        doc.toObject(Menu::class.java)
                    } catch (e: Exception) {
                        null
                    }
                } ?: emptyList()
                trySend(menus)
            }
        awaitClose { listener.remove() }
    }

    fun getAvailableMenusFlow(): Flow<List<Menu>> = callbackFlow {
        val listener = menuCollection
            .whereEqualTo("available", true)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val menus = snapshot?.documents?.mapNotNull {
                    it.toObject(Menu::class.java)
                } ?: emptyList()

                trySend(menus)
            }
        awaitClose { listener.remove() }
    }

    fun getTopMenusFlow(limit: Int = 5): Flow<List<Menu>> = callbackFlow {
        val listener = menuCollection
            .orderBy("soldCount", Query.Direction.DESCENDING)
            .limit(limit.toLong())
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val menus = snapshot?.documents?.mapNotNull { doc ->
                    try {
                        doc.toObject(Menu::class.java)
                    } catch (e: Exception) {
                        null
                    }
                } ?: emptyList()
                trySend(menus)
            }
        awaitClose { listener.remove() }
    }

    suspend fun getMenuById(menuId: String): Menu? {
        return try {
            val doc = menuCollection.document(menuId).get().await()
            doc.toObject(Menu::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun addMenu(menu: Menu): Result<String> {
        return try {
            val docRef = menuCollection.add(menu).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateMenu(menu: Menu): Result<Unit> {
        return try {
            menuCollection.document(menu.id).set(menu).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteMenu(menuId: String): Result<Unit> {
        return try {
            menuCollection.document(menuId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ==================== PESANAN OPERATIONS ====================

    fun getActivePesananFlow(): Flow<List<Pesanan>> = callbackFlow {
        val listener = pesananCollection
            .whereIn("status", listOf("Pending", "Diterima"))
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val list = snapshot?.documents
                    ?.mapNotNull { it.toObject(Pesanan::class.java) }
                    ?.sortedBy { it.createdAt }
                    ?: emptyList()

                trySend(list)
            }
        awaitClose { listener.remove() }
    }

    fun getPendingPesananFlow(): Flow<List<Pesanan>> = callbackFlow {
        val listener = pesananCollection
            .whereIn("status", listOf("Pending", "Diterima"))
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val pesananList = snapshot?.documents?.mapNotNull { doc ->
                    try {
                        doc.toObject(Pesanan::class.java)
                    } catch (e: Exception) {
                        null
                    }
                }?.sortedBy { it.createdAt } ?: emptyList()
                trySend(pesananList)
            }
        awaitClose { listener.remove() }
    }

    fun getCompletedPesananFlow(): Flow<List<Pesanan>> = callbackFlow {
        val listener = pesananCollection
            .whereEqualTo("status", "Selesai")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val list = snapshot?.documents
                    ?.mapNotNull { it.toObject(Pesanan::class.java) }
                    ?.sortedByDescending { it.createdAt }
                    ?: emptyList()

                trySend(list)
            }
        awaitClose { listener.remove() }
    }

    fun getPesananByUserFlow(userId: String): Flow<List<Pesanan>> = callbackFlow {
        val listener = pesananCollection
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val pesananList = snapshot?.documents?.mapNotNull { doc ->
                    try {
                        doc.toObject(Pesanan::class.java)
                    } catch (e: Exception) {
                        null
                    }
                }?.sortedByDescending { it.createdAt } ?: emptyList()
                trySend(pesananList)
            }
        awaitClose { listener.remove() }
    }

    suspend fun addPesanan(pesanan: Pesanan): Result<String> {
        return try {
            val docRef = pesananCollection.add(pesanan).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updatePesananStatus(pesananId: String, status: String): Result<Unit> {
        return try {
            pesananCollection.document(pesananId).update(
                mapOf(
                    "status" to status,
                    "updatedAt" to Timestamp.now()
                )
            ).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ==================== TRANSAKSI OPERATIONS ====================

    fun getTransaksiFlow(): Flow<List<Transaksi>> = callbackFlow {
        val listener = transaksiCollection
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val transaksiList = snapshot?.documents?.mapNotNull { doc ->
                    try {
                        doc.toObject(Transaksi::class.java)
                    } catch (e: Exception) {
                        null
                    }
                } ?: emptyList()
                trySend(transaksiList)
            }
        awaitClose { listener.remove() }
    }

    suspend fun getTransaksiByDateRange(startDate: Long, endDate: Long): List<Transaksi> {
        return try {
            val startTimestamp = Timestamp(Date(startDate))
            val endTimestamp = Timestamp(Date(endDate))

            val snapshot = transaksiCollection
                .whereGreaterThanOrEqualTo("createdAt", startTimestamp)
                .whereLessThanOrEqualTo("createdAt", endTimestamp)
                .get()
                .await()
            snapshot.documents.mapNotNull {
                try {
                    it.toObject(Transaksi::class.java)
                } catch (e: Exception) {
                    null
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun addTransaksi(transaksi: Transaksi): Result<String> {
        return try {
            val docRef = transaksiCollection.add(transaksi).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ==================== EVENT OPERATIONS ====================

    fun getActiveEventsFlow(): Flow<List<Event>> = callbackFlow {
        val listener = eventCollection
            .whereEqualTo("active", true)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val events = snapshot?.documents?.mapNotNull {
                    it.toObject(Event::class.java)?.copy(id = it.id)
                } ?: emptyList()

                trySend(events)
            }
        awaitClose { listener.remove() }
    }

    fun getAllEventsFlow(): Flow<List<Event>> = callbackFlow {
        val listener = eventCollection
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val events = snapshot?.documents?.mapNotNull { doc ->
                    try {
                        doc.toObject(Event::class.java)
                    } catch (e: Exception) {
                        null
                    }
                }?.sortedByDescending { it.createdAt } ?: emptyList()
                trySend(events)
            }
        awaitClose { listener.remove() }
    }

    suspend fun addEvent(event: Event): Result<String> {
        return try {
            val docRef = eventCollection.add(event).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateEvent(event: Event): Result<Unit> {
        return try {
            eventCollection.document(event.id).set(event).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteEvent(eventId: String): Result<Unit> {
        return try {
            eventCollection.document(eventId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getEventById(eventId: String): Event? {
        return try {
            val doc = eventCollection.document(eventId).get().await()
            doc.toObject(Event::class.java)?.copy(id = doc.id)
        } catch (e: Exception) {
            null
        }
    }

    // ==================== USER OPERATIONS ====================

    suspend fun getOrCreateUser(userId: String, email: String, name: String): User {
        return try {
            val doc = userCollection.document(userId).get().await()
            if (doc.exists()) {
                doc.toObject(User::class.java) ?: User(id = userId, email = email, name = name)
            } else {
                val newUser = User(id = userId, email = email, name = name)
                userCollection.document(userId).set(newUser).await()
                newUser
            }
        } catch (e: Exception) {
            User(id = userId, email = email, name = name)
        }
    }

    suspend fun updateUser(user: User): Result<Unit> {
        return try {
            userCollection.document(user.id).set(user).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ==================== DASHBOARD/STATISTICS ====================

    suspend fun getTotalRevenue(startDate: Long, endDate: Long): Int {
        return try {
            val transaksiList = getTransaksiByDateRange(startDate, endDate)
            transaksiList.sumOf { it.total }
        } catch (e: Exception) {
            0
        }
    }

    suspend fun getActiveEventsCount(): Int {
        return try {
            eventCollection
                .whereEqualTo("active", true)
                .get()
                .await()
                .size()
        } catch (e: Exception) {
            0
        }
    }
}