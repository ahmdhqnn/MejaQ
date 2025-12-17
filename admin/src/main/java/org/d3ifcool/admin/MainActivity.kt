package org.d3ifcool.admin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import org.d3ifcool.admin.navigation.SetupNavGraph
import org.d3ifcool.admin.ui.theme.MejaQTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MejaQTheme {
                SetupNavGraph()
                }
            }
        }
    }


