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

import android.os.Bundle;
import androidx.preference.PreferenceManager;
import com.android.settingslib.collapsingtoolbar.CollapsingToolbarBaseActivity;

import com.xiaomi.settings.R;

public class TurboChargingActivity extends CollapsingToolbarBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(this, R.xml.turbocharging, false);

        setContentView(R.layout.turbocharging_layout);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, new TurboChargingFragment())
                .commit();
    }
}