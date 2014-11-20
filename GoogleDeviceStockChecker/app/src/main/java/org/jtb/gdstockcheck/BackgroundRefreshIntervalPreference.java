package org.jtb.gdstockcheck;

import android.content.Context;

class BackgroundRefreshIntervalPreference extends PreferenceWrapper<Integer> {
  BackgroundRefreshIntervalPreference(Context context) {
    super(context);
  }

  @Override
  Integer get() {
    return prefs.getInt(context.getString(R.string.preference_refresh_interval_background), 60);
  }

  @Override
  void put(Integer value) {
    prefs.edit().putInt(context.getString(R.string.preference_refresh_interval_background), value).apply();
  }
}
