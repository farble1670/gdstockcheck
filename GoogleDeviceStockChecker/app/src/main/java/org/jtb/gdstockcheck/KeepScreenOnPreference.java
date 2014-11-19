package org.jtb.gdstockcheck;

import android.content.Context;

class KeepScreenOnPreference extends PreferenceWrapper<Boolean>{

  KeepScreenOnPreference(Context context) {
    super(context);
  }

  @Override
  Boolean get() {
    return prefs.getBoolean(context.getString(R.string.preference_keep_screen_on), false);
  }

  @Override
  void put(Boolean keepScreenOn) {
    prefs.edit().putBoolean(context.getString(R.string.preference_keep_screen_on), keepScreenOn).apply();
  }
}
