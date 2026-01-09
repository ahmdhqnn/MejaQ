package org.d3ifcool.admin.login

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.d3ifcool.admin.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginAdminTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    /**
     * Test 1:
     * Email & password kosong → tidak login
     */
    @Test
    fun loginAdmin_emptyEmailPassword_showError() {
        composeRule
            .onNodeWithTag("admin_login_button")            .performClick()

        composeRule
            .onNodeWithText("Login Admin")
            .assertIsDisplayed()
    }

    /**
     * Test 2:
     * Email salah → login gagal
     */
    @Test
    fun loginAdmin_wrongCredential_failed() {
        composeRule.onNodeWithTag("admin_email")
            .performTextInput("admin_salah@gmail.com")

        composeRule.onNodeWithTag("admin_password")
            .performTextInput("passwordsalah")

        composeRule.onNodeWithTag("admin_login_button")
            .performClick()

        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule
                .onAllNodesWithText("Login Admin")
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeRule
            .onNodeWithText("Login Admin")
            .assertIsDisplayed()
    }

    /**
     * Test 3:
     * Login sukses → pindah ke Beranda Admin
     */
    @Test
    fun loginAdmin_success_navigateToHome() {
        composeRule.onNodeWithTag("admin_email")
            .performTextInput("admin@mejaq.com")

        composeRule.onNodeWithTag("admin_password")
            .performTextInput("admin123")

        composeRule.onNodeWithTag("admin_login_button")
            .performClick()

        composeRule.waitUntil(timeoutMillis = 10_000) {
            composeRule
                .onAllNodesWithTag("admin_home_title")
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        composeRule
            .onNodeWithTag("admin_home_title")
            .assertIsDisplayed()
    }


    /**
     * Test 4:
     * Button saat login
     */
    @Test
    fun loginAdmin_clickLogin_disableButton() {
        composeRule.onNodeWithTag("admin_email")
            .performTextInput("admin@mejaq.com")

        composeRule.onNodeWithTag("admin_password")
            .performTextInput("admin123")

        composeRule.onNodeWithTag("admin_login_button")
            .performClick()

        composeRule
            .onNodeWithTag("admin_login_button")
            .assertIsNotEnabled()
    }

}
