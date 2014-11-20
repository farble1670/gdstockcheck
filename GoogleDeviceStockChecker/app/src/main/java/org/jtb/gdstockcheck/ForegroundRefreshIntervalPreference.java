package org.jtb.gdstockcheck;

import android.content.Context;

class ForegroundRefreshIntervalPreference extends PreferenceWrapper<Integer> {
  ForegroundRefreshIntervalPreference(Context context) {
    super(context);
  }

  @Override
  Integer get() {
    return prefs.getInt(context.getString(R.string.preference_refresh_interval_foreground), 20);
  }

  @Override
  void put(Integer value) {
    prefs.edit().putInt(context.getString(R.string.preference_refresh_interval_foreground), value).apply();
  }
}
