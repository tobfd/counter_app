package com.tobfd.counter.ui.theme

import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.tobfd.counter.SettingsDataStore

@Composable
fun counterButtonColors(): ButtonColors {
    val colorScheme = MaterialTheme.colorScheme
    val context = LocalContext.current
    val settingsDataStore = remember { SettingsDataStore(context) }
    val isDark = darkMode(settingsDataStore)
    return ButtonDefaults.buttonColors(
        containerColor = if (isDark) colorScheme.surfaceVariant else colorScheme.primary,
        contentColor = colorScheme.onPrimary
    )
}
