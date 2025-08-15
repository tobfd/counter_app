package com.tobfd.counter

import android.os.VibrationEffect
import android.os.VibratorManager
import android.content.Context
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import kotlinx.coroutines.flow.first

object HapticUtils {
    suspend fun performHapticFeedback(
        context: Context? = null,
        haptic: HapticFeedback? = null,
        isSwitch: Boolean = false,
        settingsDataStore: SettingsDataStore
    ) {
        if (settingsDataStore.hapticFeedbackFlow.first()) {
            if (isSwitch) {
                val vibratorManager = context?.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                val vibrator = vibratorManager.defaultVibrator
                vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK))
            } else {
                haptic?.performHapticFeedback(HapticFeedbackType.LongPress)
            }
        }
    }
}
