package org.d3ifcool.dapur.login

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.d3ifcool.dapur.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginScreenTest {

    companion object {
        const val EMAIL = "dapur@mejaq.com"
        const val PASSWORD = "dapur123"
    }

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun login_dapur_berhasil() {

        composeTestRule
            .onNodeWithText("Email")
            .performTextInput(EMAIL)

        composeTestRule
            .onNodeWithText("Password")
            .performTextInput(PASSWORD)

        composeTestRule
            .onNodeWithText("Login")
            .performClick()

        composeTestRule.waitUntil(
            timeoutMillis = 10_000
        ) {
            composeTestRule
                .onAllNodesWithText("Pesanan")
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule
            .onNodeWithText("Pesanan")
            .assertExists()
    }
}

