package org.jtb.gdstockcheck;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.Callable;

class CheckStatus implements Callable<Status> {
  private final Device device;

  CheckStatus(Device device) {
    this.device = device;
  }

  @Override
  public Status call() {
    try {
      URL url = new URL(device.pageUrl);
      InputStream in = url.openStream();
      BufferedReader reader = new BufferedReader(new InputStreamReader(in));
      StringBuilder result = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        result.append(line);
      }

      return org.jtb.gdstockcheck.Status.getStatus(result.toString());
    } catch (Exception e) {
      return org.jtb.gdstockcheck.Status.UNKNOWN;
    }
  }
}
