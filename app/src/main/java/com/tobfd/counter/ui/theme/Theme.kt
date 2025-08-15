package com.tobfd.counter.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.tobfd.counter.SettingsDataStore

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun getUseDarkTheme(settingsDataStore: SettingsDataStore?): Boolean {
    val systemThemeIsDark = isSystemInDarkTheme()
    if (settingsDataStore == null) {
        return systemThemeIsDark
    }
    val appThemePreference by settingsDataStore.appThemeFlow.collectAsState(initial = "system")
    return when (appThemePreference) {
        "dark_mode" -> true
        "light_mode" -> false
        else -> systemThemeIsDark
    }
}

@Composable
fun CounterTheme(
    dynamicColor: Boolean = true,
    settingsDataStore: SettingsDataStore?,
    content: @Composable () -> Unit
) {
    val useDarkTheme: Boolean = if (settingsDataStore != null) {
        val systemThemeIsDark = isSystemInDarkTheme()
        val appThemePreference by settingsDataStore.appThemeFlow.collectAsState(initial = "system")
        when (appThemePreference) {
            "dark_mode" -> true
            "light_mode" -> false
            else -> systemThemeIsDark
        }
    } else {
        isSystemInDarkTheme()
    }

    val colorScheme = when {
        dynamicColor -> {
            val context = LocalContext.current
            if (useDarkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        useDarkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
