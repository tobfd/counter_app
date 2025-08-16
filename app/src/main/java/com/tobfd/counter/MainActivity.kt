package com.tobfd.counter

import android.content.Intent
import android.os.Bundle
import androidx.compose.runtime.collectAsState
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.icons.filled.Settings
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tobfd.counter.ui.theme.CounterTheme
import com.tobfd.counter.ui.theme.counterButtonColors

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val settingsDataStore = remember { SettingsDataStore(context) }
            CounterTheme(settingsDataStore = settingsDataStore) {
                CounterButton()
            }
        }
    }
}


@Composable
fun CounterButton() {
    val count = remember { mutableIntStateOf(0) }
    val haptic = LocalHapticFeedback.current
    val colorScheme = MaterialTheme.colorScheme
    val stepSize = remember { mutableIntStateOf(1) }
    val multiplierIsExpanded = remember { mutableStateOf(false) }
    val customTextInput = remember { mutableStateOf("") }
    val snackBarHostState = remember { SnackbarHostState() }
    val showSnackBar = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val settingsDataStore = remember { SettingsDataStore(context) }

    val showMultiplierResetButton by settingsDataStore.showResetButtonFlow.collectAsState(initial = true)
    val animations by settingsDataStore.showAnimationsFlow.collectAsState(initial = true)
    val triggerHapticFeedback = remember { mutableStateOf(false) }
    val preselectableSteps = remember(showMultiplierResetButton) {
        if (showMultiplierResetButton) listOf(-10, -5, 5, 10)
        else listOf(-10, -5, 1, 5, 10)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.counter_top_spacing)))
            Text(
                text = "${count.intValue}",
                color = colorScheme.onBackground,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))

            Row {
                Button(onClick = {
                    count.intValue -= stepSize.intValue
                }, colors = counterButtonColors()) {
                    Text(stringResource(R.string.decrement), color = Color.Yellow)
                }
                Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_medium)))
                Button(onClick = {
                    count.intValue += stepSize.intValue
                }, colors = counterButtonColors()) {
                    Text(stringResource(R.string.increment), color = Color.Green)
                }
                Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_medium)))
                Button(onClick = {
                    triggerHapticFeedback.value = true
                    count.intValue = 0
                }, enabled = count.intValue != 0, colors = counterButtonColors()) {
                    Text(stringResource(R.string.reset), color = Color.Red)
                }
            }

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_xlarge)))

            val arrowRotation by animateFloatAsState(
                targetValue = if (multiplierIsExpanded.value) 360f else 270f,
                label = "MultiplierIconRotation"
            )

            Button(onClick = {
                multiplierIsExpanded.value = !multiplierIsExpanded.value
            }, colors = counterButtonColors()) {
                Text("${stringResource(R.string.multiplier)} ${stepSize.intValue}", color = colorScheme.secondary)
                Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_small)))
                Icon(imageVector = if (animations) Icons.Filled.ArrowDropDown else if (multiplierIsExpanded.value) Icons.Filled.ArrowDropDown else Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = if (multiplierIsExpanded.value) "Collapse multiplier options" else "Expand multiplier options",
                    tint = colorScheme.secondary,
                    modifier = if (animations) Modifier.rotate(arrowRotation) else Modifier
                )
            }

            if (multiplierIsExpanded.value) {
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_medium)))

                Row {
                    for (step in preselectableSteps) {
                        Button(onClick = {
                            stepSize.intValue = step
                        }, colors = counterButtonColors(), enabled = stepSize.intValue != step) {
                            Text(step.toString(), color = colorScheme.secondary)
                        }
                        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_small)))
                    }
                }

                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_small)))

                if (showMultiplierResetButton) {
                    Row {
                        Button(onClick = {
                            triggerHapticFeedback.value = true
                            stepSize.intValue = 1
                        }, colors = counterButtonColors(), enabled = stepSize.intValue != 1) {
                            Text(stringResource(R.string.reset), color = Color.Red)
                        }
                    }
                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_small)))
                }

                Row {
                    TextField(
                        value = customTextInput.value,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        onValueChange = {
                            customTextInput.value = it
                        },
                        label = { Text(stringResource(R.string.custom)) },
                        singleLine = true,
                        modifier = Modifier.width(140.dp)
                    )
                    Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_small)))
                    Button(onClick = {
                        val value = customTextInput.value.toIntOrNull()
                        if (value != null) {
                            stepSize.intValue = value
                            customTextInput.value = ""
                        } else {
                            showSnackBar.value = true
                        }
                    }, colors = counterButtonColors(), enabled = customTextInput.value != "") {
                        Text(stringResource(R.string.set), color = colorScheme.secondary)
                    }
                    val invalidNumberText: String = stringResource(R.string.invalidNumber)
                    if (showSnackBar.value) {
                        LaunchedEffect(Unit) {
                            customTextInput.value = ""
                            snackBarHostState.showSnackbar(message = invalidNumberText)
                            showSnackBar.value = false
                        }
                    }
                }
            }

            if (triggerHapticFeedback.value) {
                LaunchedEffect(Unit) {
                    HapticUtils.performHapticFeedback(haptic = haptic, settingsDataStore = settingsDataStore)
                    triggerHapticFeedback.value = false
                }
            }
        }

        IconButton(
            onClick = {
                val intent = Intent(context, Settings::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 40.dp)
                .padding(dimensionResource(R.dimen.spacing_medium))
        ) {
            Icon(
                imageVector = Icons.Filled.Settings,
                contentDescription = stringResource(R.string.settings),
                tint = colorScheme.secondary
            )
        }

        SnackbarHost(
            hostState = snackBarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(WindowInsets.ime.asPaddingValues())
                .padding(bottom = dimensionResource(R.dimen.spacing_large))
        ) { data ->
            androidx.compose.material3.Snackbar(
                snackbarData = data,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                shape = RoundedCornerShape(16.dp)
            )
        }
    }
}
