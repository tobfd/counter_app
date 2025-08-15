package com.tobfd.counter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.collectAsState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.rounded.Autorenew
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Switch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.launch
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tobfd.counter.HapticUtils.performHapticFeedback
import com.tobfd.counter.ui.theme.CounterTheme
import com.tobfd.counter.ui.theme.getUseDarkTheme

class Settings : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            CounterTheme(settingsDataStore = SettingsDataStore(context)) {
                Settings(onBack = { finish() })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(onBack: () -> Unit) {
    val context = LocalContext.current
    val settingsDataStore = remember { SettingsDataStore(context) }
    val showResetButton by settingsDataStore.showResetButtonFlow.collectAsState(initial = true)
    val showAnimations by settingsDataStore.showAnimationsFlow.collectAsState(initial = true)
    val hapticFeedback by settingsDataStore.hapticFeedbackFlow.collectAsState(initial = true)
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.settings)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.showresetbutton),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Switch(
                        checked = showResetButton,
                        onCheckedChange = {
                            scope.launch {
                                performHapticFeedback(context = context, settingsDataStore = settingsDataStore, isSwitch = true)
                                settingsDataStore.updateShowResetButton(it)
                            }
                        }
                    )
                }
                Text(
                    text = stringResource(R.string.showresetbuttondescription),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .fillMaxWidth(0.85f)
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.showanimation),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Switch(
                        checked = showAnimations,
                        onCheckedChange = {
                            scope.launch {
                                performHapticFeedback(context = context, settingsDataStore = settingsDataStore, isSwitch = true)
                                settingsDataStore.updateShowAnimations(it)
                            }
                        }
                    )
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.hapticfeedback),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Switch(
                        checked = hapticFeedback,
                        onCheckedChange = {
                            scope.launch {
                                performHapticFeedback(context = context, settingsDataStore = settingsDataStore, isSwitch = true)
                                settingsDataStore.updateHapticFeedback(it)
                            }
                        }
                    )
                }
            }

            var themeDropdownExpanded by remember { mutableStateOf(false) }

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                val haptic = LocalHapticFeedback.current
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.apptheme),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Box(modifier = Modifier.wrapContentSize(Alignment.TopEnd)) {
                        Button(onClick = { themeDropdownExpanded = true }) {
                            if (getUseDarkTheme(settingsDataStore)) Icon(Icons.Rounded.DarkMode, contentDescription = stringResource(R.string.darkmode)) else Icon(Icons.Rounded.LightMode, contentDescription = stringResource(R.string.lightmode))
                        }
                        DropdownMenu(expanded = themeDropdownExpanded, onDismissRequest = { themeDropdownExpanded = false }) {
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.darkmode)) },
                                onClick = {
                                    themeDropdownExpanded = false
                                    scope.launch {
                                        performHapticFeedback(settingsDataStore = settingsDataStore, haptic = haptic)
                                        settingsDataStore.updateAppTheme("dark_mode")
                                    }
                                },
                                leadingIcon = { Icon(Icons.Rounded.DarkMode, contentDescription = stringResource(R.string.darkmode)) }
                            )
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.lightmode)) },
                                onClick = {
                                    themeDropdownExpanded = false
                                    scope.launch {
                                        performHapticFeedback(settingsDataStore = settingsDataStore, haptic = haptic)
                                        settingsDataStore.updateAppTheme("light_mode")
                                    }
                                },
                                leadingIcon = { Icon(Icons.Rounded.LightMode, contentDescription = stringResource(R.string.lightmode)) }
                            )
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.system)) },
                                onClick = {
                                    themeDropdownExpanded = false
                                    scope.launch {
                                        performHapticFeedback(settingsDataStore = settingsDataStore, haptic = haptic)
                                        settingsDataStore.updateAppTheme("system")
                                    }
                                },
                                leadingIcon = { Icon(Icons.Rounded.Autorenew, contentDescription = stringResource(R.string.system)) }
                            )
                        }
                    }
                }
            }
        }
    }
}
