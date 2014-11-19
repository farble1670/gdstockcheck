package org.jtb.gdstockcheck;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

class DevicesPreference {
  private final Context context;
  private final SharedPreferences prefs;

  DevicesPreference(Context context) {
    this.context = context;
    prefs = PreferenceManager.getDefaultSharedPreferences(context);
  }

  List<Device> get() {
    List<Device> devices;
    if (prefs.contains(context.getString(R.string.preference_devices))) {
      Set<String> ds = prefs.getStringSet(context.getString(R.string.preference_devices), null);
      devices = new ArrayList<Device>();
      for (String d: ds) {
        Device device = Device.valueOf(d);
        devices.add(device);
      }
    } else {
      devices = new ArrayList<Device>(Arrays.asList(Device.values()));
      set(devices);
    }

    Collections.sort(devices, new Comparator<Device>() {
      @Override
      public int compare(Device lhs, Device rhs) {
        return lhs.name().compareTo(rhs.name());
      }
    });
    return devices;
  }

  void set(Collection<Device> devices) {
    Set<String> ds = new LinkedHashSet<String>();
    for (Device device: devices) {
      ds.add(device.name());
    }
    prefs.edit().putStringSet(context.getString(R.string.preference_devices), ds).apply();
  }
}
