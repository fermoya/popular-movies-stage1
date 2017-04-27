package com.example.fmoyader.popularmovies.preferences;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.preference.PreferenceScreen;

import com.example.fmoyader.popularmovies.R;

/**
 * Created by fmoyader on 7/4/17.
 */

public class SettingsFragment extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    public void onCreatePreferences(Bundle bundle, String s) {

        // Add visualizer preferences, defined in the XML file in res->xml->pref_visualizer
        addPreferencesFromResource(R.xml.pref_popular_movies);


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        PreferenceScreen preferenceScreen = getPreferenceScreen();

        int count = preferenceScreen.getPreferenceCount();
        for (int i = 0; i < count; i++) {
            if (preferenceScreen.getPreference(i) instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preferenceScreen.getPreference(i);
                listPreference.setSummary(
                        sharedPreferences.getString(
                                getString(R.string.pref_sort_by_list_key),
                                getString(R.string.pref_sort_by_popularity)
                        ));
            }
        }

        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);
        if (null != preference) {
            // Updates the summary for the preference
            if (preference instanceof ListPreference) {
                String value = sharedPreferences.getString(
                        preference.getKey(),
                        getString(R.string.pref_sort_by_popularity)
                );

                preference.setSummary(value);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }
}
