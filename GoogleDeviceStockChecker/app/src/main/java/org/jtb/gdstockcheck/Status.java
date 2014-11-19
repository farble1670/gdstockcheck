package org.jtb.gdstockcheck;

import android.graphics.Color;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

enum Status {
  OUT_OF_INVENTORY(Pattern.compile("out of inventory", Pattern.CASE_INSENSITIVE), R.string.status_out_of_inventory, Color.RED),
  IN_STOCK(Pattern.compile("in stock", Pattern.CASE_INSENSITIVE), R.string.status_in_stock, Color.GREEN),
  LOADING(null, R.string.status_loading, Color.BLACK),
  UNKNOWN(null, R.string.status_unknown, Color.BLACK);

  private final Pattern pattern;
  final int stringId;
  final int color;

  private Status(Pattern pattern, int stringId, int color) {
    this.pattern = pattern;
    this.stringId = stringId;
    this.color = color;
  }

  static Status getStatus(String input) {
    for (Status status : values()) {
      if (status.pattern == null) {
        continue;
      }
      Matcher m = status.pattern.matcher(input);
      if (m.find()) {
        return status;
      }
    }
    return UNKNOWN;
  }
}
