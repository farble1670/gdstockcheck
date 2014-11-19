package org.jtb.gdstockcheck;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

class KeepScreenOnPreference {
  private final Context context;
  private final SharedPreferences prefs;

  KeepScreenOnPreference(Context context) {
    this.context = context;
    prefs = PreferenceManager.getDefaultSharedPreferences(context);
  }

  boolean isKeepScreenOn() {
    return prefs.getBoolean(context.getString(R.string.preference_keep_screen_on), false);
  }

  void setKeepScreenOn(boolean keepScreenOn) {
    prefs.edit().putBoolean(context.getString(R.string.preference_keep_screen_on), keepScreenOn).apply();
  }
}
