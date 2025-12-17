package org.d3ifcool.admin.menu

import androidx.lifecycle.ViewModel
import org.d3ifcool.admin.R
import org.d3ifcool.shared.model.Menu

class MainViewModel : ViewModel() {

    val data = listOf(
        Menu(1, "Indomie Goreng", "Indomie goreng + telur", 10000, 0, R.drawable.mie_goreng),
        Menu(2, "Indomie Rebus", "Indomie rebus + telur", 10000, 0, R.drawable.mie_rebus),
        Menu(3, "Nasi Goreng", "Nasi goreng spesial", 15000, 0, R.drawable.nasi_goreng),
        Menu(4, "Magelangan", "Nasi + Mie goreng", 18000, 0, R.drawable.magelangan),
        Menu(5, "Es Teh Manis", "Teh manis dingin", 5000, 0, R.drawable.es_teh),
    )
}
