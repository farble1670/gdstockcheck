package org.jtb.gdstockcheck;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

class DevicesPreference extends PreferenceWrapper<List<Device>> {

  DevicesPreference(Context context) {
    super(context);
  }

  @Override
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
      put(devices);
    }

    Collections.sort(devices, new Comparator<Device>() {
      @Override
      public int compare(Device lhs, Device rhs) {
        return lhs.name().compareTo(rhs.name());
      }
    });
    return devices;
  }

  List<String> getNames() {
    List<Device> devices = get();
    List<String> strings = new ArrayList<String>();
    for (Device device: devices) {
      strings.add(device.name);
    }
    return strings;
  }

  @Override
  void put(List<Device> devices) {
    Set<String> ds = new LinkedHashSet<String>();
    for (Device device: devices) {
      ds.add(device.name());
    }
    prefs.edit().putStringSet(context.getString(R.string.preference_devices), ds).apply();
  }
}
