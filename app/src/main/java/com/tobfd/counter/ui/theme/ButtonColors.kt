package com.tobfd.counter.ui.theme

import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.luminance

@Composable
fun counterButtonColors(): ButtonColors {
    val colorScheme = MaterialTheme.colorScheme
    val isDark = colorScheme.background.luminance() < 0.5f
    return ButtonDefaults.buttonColors(
        containerColor = if (isDark) colorScheme.surfaceVariant else colorScheme.primary,
        contentColor = colorScheme.onPrimary
    )
}
