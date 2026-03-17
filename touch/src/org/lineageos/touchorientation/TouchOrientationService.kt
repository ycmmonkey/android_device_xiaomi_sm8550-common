/*
 * SPDX-FileCopyrightText: 2023-2025 Paranoid Android
 * SPDX-FileCopyrightText: 2026 The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package org.lineageos.touchorientation

import android.app.Service
import android.content.Intent
import android.content.res.Configuration
import android.hardware.display.DisplayManager
import android.os.IBinder
import android.util.Log
import android.view.Display
import java.io.File

class TouchOrientationService : Service() {

    companion object {
        private const val TAG = "TouchOrientationService"
        private val DEBUG = Log.isLoggable(TAG, Log.DEBUG)

        private const val TOUCH_PANEL_ORIENTATION_PATH = "/sys/class/touch/touch_dev/panel_orientation"
    }

    private val displayManager by lazy { getSystemService(DisplayManager::class.java) }

    private var rotation: Int = -1
        set(value) {
            if (field == value) return
            field = value
            if (DEBUG) Log.d(TAG, "rotation=$value")
            runCatching {
                File(TOUCH_PANEL_ORIENTATION_PATH).writeText("$value\n")
            }.onFailure { e ->
                Log.e(TAG, "Failed to set touch panel orientation", e)
            }
        }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (DEBUG) Log.d(TAG, "onStartCommand")
        updateOrientation()
        return START_STICKY
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (DEBUG) Log.d(TAG, "onConfigurationChanged")
        updateOrientation()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun updateOrientation() {
        val value = displayManager?.getDisplay(Display.DEFAULT_DISPLAY)?.rotation ?: return
        rotation = value
    }
}
