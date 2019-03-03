package dk.kugelberg.hoek_helper.view;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import dk.kugelberg.hoek_helper.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
//        setPreferencesFromResource(R.xml.settings, rootKey);
        addPreferencesFromResource(R.xml.settings);
    }

}