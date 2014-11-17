package org.jtb.gdstockcheck;

import org.jtb.gdstockcheck.R;

enum Device {
  G_WATCH_R("https://play.google.com/store/devices/details?id=lg_g_watch_r_black", R.drawable.lg_gwatch_r, "G Watch R"),
  ;

  public final String pageUrl;
  public final int imageId;
  public final String name;

  private Device(String pageUrl, int imageId, String name) {
    this.pageUrl = pageUrl;
    this.imageId = imageId;
    this.name = name;
  }
}
