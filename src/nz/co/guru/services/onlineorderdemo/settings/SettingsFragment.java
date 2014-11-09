package nz.co.guru.services.onlineorderdemo.settings;

import nz.co.guru.services.onlineorderdemo.R;
import android.os.Bundle;
import android.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ---load the preferences from an XML file---
        addPreferencesFromResource(R.xml.preferences);
    }
}
