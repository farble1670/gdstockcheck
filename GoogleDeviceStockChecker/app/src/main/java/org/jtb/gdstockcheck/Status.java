package org.jtb.gdstockcheck;

import android.graphics.Color;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

enum Status {
  OUT_OF_INVENTORY(Pattern.compile("out of inventory", Pattern.CASE_INSENSITIVE), Color.RED),
  IN_STOCK(Pattern.compile("in stock", Pattern.CASE_INSENSITIVE), Color.GREEN),
  UNKNOWN(Pattern.compile(".*", Pattern.CASE_INSENSITIVE), Color.BLACK);

  private final Pattern pattern;
  final int color;

  private Status(Pattern pattern, int color) {
    this.pattern = pattern;
    this.color = color;
  }

  static Status getStatus(String input) {
    for (Status status : values()) {
      Matcher m = status.pattern.matcher(input);
      if (m.find()) {
        return status;
      }
    }
    return UNKNOWN;
  }
}
