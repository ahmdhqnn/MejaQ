package org.d3ifcool.mejaq

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import org.d3ifcool.mejaq.login.UserApp
import org.d3ifcool.mejaq.navigation.SetupNavGraph
import org.d3ifcool.mejaq.ui.theme.MejaqAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MejaqAppTheme {
                UserApp()
            }
        }
    }
}


