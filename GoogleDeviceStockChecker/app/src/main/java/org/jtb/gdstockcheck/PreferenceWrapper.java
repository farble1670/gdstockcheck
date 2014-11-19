package org.jtb.gdstockcheck;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

abstract class PreferenceWrapper<T> {
  protected final Context context;
  protected final SharedPreferences prefs;

  PreferenceWrapper(Context context) {
    this.context = context;
    prefs = PreferenceManager.getDefaultSharedPreferences(context);
  }

  abstract T get();
  abstract void put(T value);
}
