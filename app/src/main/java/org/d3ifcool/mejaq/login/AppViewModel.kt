package org.d3ifcool.mejaq.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.d3ifcool.shared.util.SharedUtil

class AppViewModel: ViewModel() {
    val userFlow= SharedUtil.getUserFlow(viewModelScope)
}