package com.joetimmins.watchbox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.joetimmins.watchbox.ui.search.SearchScreenContent
import com.joetimmins.watchbox.ui.theme.WatchboxTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WatchboxTheme {
                SearchScreenContent()
            }
        }
    }
}
