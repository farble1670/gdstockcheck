package org.jtb.gdstockcheck;

interface StatusListener {
  void onStatus(Device device, Status status);
}
