/*
 * Copyright (C) 2025 kenway214
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

package com.xiaomi.settings.turbocharging;

import android.content.SharedPreferences;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.widget.Toast;

import androidx.preference.PreferenceManager;

import com.xiaomi.settings.R;

public class TurboChargingTile extends TileService {

    private static final String PREF_TURBO_ENABLED = "turbo_enable";

    @Override
    public void onClick() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean turboEnabled = prefs.getBoolean(PREF_TURBO_ENABLED, false);
        boolean newState = !turboEnabled;
        prefs.edit().putBoolean(PREF_TURBO_ENABLED, newState).apply();
        TurboChargingUtil.applyTurboSetting(this);
        updateTileState();
        Toast.makeText(this,
                newState ? getString(R.string.toast_turbo_on) : getString(R.string.toast_turbo_off),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
        updateTileState();
    }

    private void updateTileState() {
        Tile tile = getQsTile();
        if (tile == null) return;

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean turboEnabled = prefs.getBoolean(PREF_TURBO_ENABLED, false);
        tile.setState(turboEnabled ? Tile.STATE_ACTIVE : Tile.STATE_INACTIVE);
        tile.setLabel(getString(R.string.turbo_charge_title)); // Main label

        if (turboEnabled) {
            tile.setSubtitle(getString(R.string.tile_turbo_on)); // Show "On"
        } else {
            tile.setSubtitle(getString(R.string.tile_turbo_off)); // Show "Off"
        }

        tile.updateTile();
    }
}