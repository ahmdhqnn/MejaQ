package org.d3ifcool.shared.auth

object LoginValidator {

    fun isValid(email: String, password: String): Boolean {
        if (email.isBlank()) return false
        if (password.isBlank()) return false
        if (!email.contains("@")) return false
        if (password.length < 6) return false
        return true
    }
}
