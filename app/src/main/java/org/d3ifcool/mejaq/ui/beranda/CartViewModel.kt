package org.d3ifcool.mejaq.ui.beranda

import androidx.lifecycle.ViewModel
import org.d3ifcool.shared.R
import org.d3ifcool.shared.model.Catatan

class CartViewModel : ViewModel() {

    val data = listOf(
        Catatan(
            id = 1,
            foto = R.drawable.images1,
            catatan = "Tech Innovation Talk\nAcara seminar tentang inovasi teknologi terkini dan tren AI di dunia industri.",
            dekskripsi = "Seminar ini bertujuan untuk memperkenalkan inovasi terbaru dalam dunia teknologi, terutama tentang kecerdasan buatan (AI). Pembicara ahli akan membahas tren AI terkini dan bagaimana teknologi ini memengaruhi industri di berbagai sektor.",
            tanggal = "2025-03-10 09:00:00"
        ),
        Catatan(
            id = 2,
            foto = R.drawable.images2,
            catatan = "Workshop UI/UX Design\nPelatihan intensif tentang cara membuat desain antarmuka yang menarik dan ramah pengguna.",
            dekskripsi = "Workshop ini dirancang untuk memberikan pemahaman mendalam tentang desain antarmuka pengguna (UI) dan pengalaman pengguna (UX). Peserta akan belajar cara membuat desain yang tidak hanya estetis, tetapi juga mudah digunakan oleh pengguna akhir.",
            tanggal = "2025-03-15 13:30:00"
        ),
        Catatan(
            id = 3,
            foto = R.drawable.images3,
            catatan = "Startup Expo 2025\nPameran startup mahasiswa yang menampilkan berbagai produk digital inovatif.",
            dekskripsi = "Startup Expo 2025 adalah acara yang mengumpulkan startup mahasiswa dari berbagai universitas untuk memamerkan produk dan inovasi digital mereka. Acara ini bertujuan untuk mendukung pertumbuhan wirausaha muda dan memberikan mereka kesempatan untuk berjejaring dengan para profesional industri.",
            tanggal = "2025-03-22 10:00:00"
        )
    )
}