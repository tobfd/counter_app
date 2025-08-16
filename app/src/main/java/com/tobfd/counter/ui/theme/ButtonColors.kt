package com.tobfd.counter.ui.theme

import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.tobfd.counter.SettingsDataStore

@Composable
fun counterButtonColors(): ButtonColors {
    val colorScheme = MaterialTheme.colorScheme
    val context = LocalContext.current
    val settingsDataStore = remember { SettingsDataStore(context) }
    val isDark = getUseDarkTheme(settingsDataStore)

    val darkButtonColor = Color(0xFF2D2D2D)
    val darkButtonContentColor = Color.White

    return ButtonDefaults.buttonColors(
        containerColor = if (isDark) darkButtonColor else Color(0xFF404040),
        contentColor = darkButtonContentColor
    )
}
