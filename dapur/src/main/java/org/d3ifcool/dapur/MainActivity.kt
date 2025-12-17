package org.d3ifcool.dapur

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import org.d3ifcool.dapur.navigation.SetupNavGraph
import org.d3ifcool.dapur.theme.MejaQTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MejaQTheme {
                SetupNavGraph()
            }
        }
    }
}
