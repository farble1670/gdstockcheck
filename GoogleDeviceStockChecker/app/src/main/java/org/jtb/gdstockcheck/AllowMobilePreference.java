package org.jtb.gdstockcheck;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

class AllowMobilePreference {
  private final Context context;
  private final SharedPreferences prefs;

  AllowMobilePreference(Context context) {
    this.context = context;
    prefs = PreferenceManager.getDefaultSharedPreferences(context);
  }

  boolean isAllowMobile() {
    return prefs.getBoolean(context.getString(R.string.preference_allow_mobile), false);
  }

  void setAllowMobile(boolean allowMobile) {
    prefs.edit().putBoolean(context.getString(R.string.preference_allow_mobile), allowMobile).apply();
  }
}
