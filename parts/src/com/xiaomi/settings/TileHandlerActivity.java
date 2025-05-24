/*
 * Copyright (C) 2025 The LineageOS Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xiaomi.settings;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.service.quicksettings.TileService;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import com.xiaomi.settings.autohbm.AutoHbmActivity;
import com.xiaomi.settings.autohbm.AutoHbmTileService;
import com.xiaomi.settings.autohbm.HbmTileService;

import com.xiaomi.settings.turbocharging.TurboChargingTile;
import com.xiaomi.settings.turbocharging.TurboChargingActivity;

import com.xiaomi.settings.thermal.ThermalSettingsActivity;
import com.xiaomi.settings.thermal.ThermalTileService;

public final class TileHandlerActivity extends Activity {
    private static final String TAG = "TileHandlerActivity";

    // Map QS Tile services to their corresponding activity
    private static final Map<String, Class<?>> TILE_ACTIVITY_MAP = new HashMap<>();

    static {
        TILE_ACTIVITY_MAP.put(AutoHbmTileService.class.getName(), AutoHbmActivity.class);
        TILE_ACTIVITY_MAP.put(HbmTileService.class.getName(), AutoHbmActivity.class);
        TILE_ACTIVITY_MAP.put(TurboChargingTile.class.getName(), TurboChargingActivity.class);
        TILE_ACTIVITY_MAP.put(ThermalTileService.class.getName(), ThermalSettingsActivity.class);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent intent = getIntent();
        if (intent == null || !TileService.ACTION_QS_TILE_PREFERENCES.equals(intent.getAction())) {
            Log.e(TAG, "Invalid or null intent received");
            finish();
            return;
        }

        final ComponentName qsTile = intent.getParcelableExtra(Intent.EXTRA_COMPONENT_NAME);
        if (qsTile == null) {
            Log.e(TAG, "No QS tile component found in intent");
            finish();
            return;
        }

        final String qsName = qsTile.getClassName();
        final Intent targetIntent = new Intent();

        if (TILE_ACTIVITY_MAP.containsKey(qsName)) {
            targetIntent.setClass(this, TILE_ACTIVITY_MAP.get(qsName));
            Log.d(TAG, "Launching settings activity for QS tile: " + qsName);
        } else {
            // Default: Open app settings for the QS tile's package
            final String packageName = qsTile.getPackageName();
            if (packageName == null) {
                Log.e(TAG, "QS tile package name is null");
                finish();
                return;
            }
            targetIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            targetIntent.setData(Uri.fromParts("package", packageName, null));
            Log.d(TAG, "Opening app info for package: " + packageName);
        }

        targetIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(targetIntent);
        finish();
    }
}