package org.jtb.gdstockcheck;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SettingsFragment extends PreferenceFragment {
  private static final Handler handler = new Handler();

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    addPreferencesFromResource(R.xml.settings);

    getPreferenceManager().findPreference(getString(R.string.preference_devices)).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
      @Override
      public boolean onPreferenceChange(Preference preference, final Object newValue) {
        new AsyncTask<Void, Void, Void>() {
          @Override
          protected Void doInBackground(Void... params) {
            getActivity().getContentResolver().delete(DeviceStatusContract.DeviceStatus.CONTENT_URI, null, null);
            return null;
          }

          @Override
          protected void onPostExecute(Void aVoid) {
            setDevicesSummary((Set<String>) newValue);
          }
        }.execute();
        return true;
      }
    });

    getPreferenceManager().findPreference(getString(R.string.preference_refresh_interval_foreground)).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
      @Override
      public boolean onPreferenceChange(Preference preference, final Object newValue) {
        setForegroundRefreshIntervalSummary((Integer) newValue);
        return true;
      }
    });

    getPreferenceManager().findPreference(getString(R.string.preference_refresh_interval_background)).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
      @Override
      public boolean onPreferenceChange(Preference preference, final Object newValue) {
        setBackgroundRefreshIntervalSummary((Integer) newValue);
        return true;
      }
    });


    setDevicesSummary(PreferenceManager.getDefaultSharedPreferences(getActivity()).getStringSet(getString(R.string.preference_devices), null));
    setForegroundRefreshIntervalSummary(PreferenceManager.getDefaultSharedPreferences(getActivity()).getInt(getString(R.string.preference_refresh_interval_foreground), 20));
    setBackgroundRefreshIntervalSummary(PreferenceManager.getDefaultSharedPreferences(getActivity()).getInt(getString(R.string.preference_refresh_interval_background), 60));
  }

  private void setDevicesSummary(Set<String> values) {
    List<String> names = new ArrayList<String>();
    for (String s: values) {
      Device d = Device.valueOf(s);
      names.add(d.name);
    }
    findPreference(getString(R.string.preference_devices)).setSummary(Joiner.on("\n").join(names));
  }

  private void setForegroundRefreshIntervalSummary(int value) {
    String summary = String.format("%d seconds", value);
    findPreference(getString(R.string.preference_refresh_interval_foreground)).setSummary(summary);
  }

  private void setBackgroundRefreshIntervalSummary(int value) {
    String summary = String.format("%d seconds", value);
    findPreference(getString(R.string.preference_refresh_interval_background)).setSummary(summary);
  }
}
