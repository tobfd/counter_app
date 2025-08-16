package com.tobfd.counter.ui.theme

import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.tobfd.counter.SettingsDataStore

@Composable
fun counterButtonColors(): ButtonColors {
    val context = LocalContext.current
    val settingsDataStore = remember { SettingsDataStore(context) }
    val isDark = getUseDarkTheme(settingsDataStore)

    val darkButtonContentColor = Color.White

    return ButtonDefaults.buttonColors(
        containerColor = if (isDark) darkButtonColor else lightButtonColor,
        contentColor = darkButtonContentColor
    )
}
