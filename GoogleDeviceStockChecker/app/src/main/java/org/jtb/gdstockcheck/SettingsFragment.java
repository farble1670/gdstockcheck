package org.jtb.gdstockcheck;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import com.google.common.base.Joiner;

public class SettingsFragment extends PreferenceFragment {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    addPreferencesFromResource(R.xml.settings);

    getPreferenceManager().findPreference(getString(R.string.preference_devices)).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
      @Override
      public boolean onPreferenceChange(Preference preference, Object newValue) {
        new AsyncTask<Void,Void,Void>() {
          @Override
          protected Void doInBackground(Void... params) {
            getActivity().getContentResolver().delete(DeviceStatusContract.DeviceStatus.CONTENT_URI, null, null);
            return null;
          }

          @Override
          protected void onPostExecute(Void aVoid) {
            setDevicesSummary();
          }
        }.execute();
        return true;
      }
    });

    setDevicesSummary();
  }

  private void setDevicesSummary() {
    findPreference(getString(R.string.preference_devices)).setSummary(Joiner.on(", ").join(new DevicesPreference(getActivity()).getNames()));
  }
}
