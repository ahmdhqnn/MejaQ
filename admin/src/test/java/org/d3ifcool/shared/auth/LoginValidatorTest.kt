package org.d3ifcool.shared.auth

import org.junit.Assert.*
import org.junit.Test

class LoginValidatorTest {

    @Test
    fun email_dan_password_valid() {
        val result = LoginValidator.isValid(
            email = "dapur@mejaq.com",
            password = "dapur123"
        )
        assertTrue(result)
    }

    @Test
    fun email_kosong_tidak_valid() {
        val result = LoginValidator.isValid(
            email = "",
            password = "dapur123"
        )
        assertFalse(result)
    }

    @Test
    fun password_pendek_tidak_valid() {
        val result = LoginValidator.isValid(
            email = "dapur@mejaq.com",
            password = "123"
        )
        assertFalse(result)
    }
}
