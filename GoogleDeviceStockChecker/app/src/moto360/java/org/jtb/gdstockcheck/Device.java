package org.jtb.gdstockcheck;

enum Device {
  MOTO_360_BLACK("https://play.google.com/store/devices/details/Moto_360_Black_Leather?id=motorola_moto_360_leather_black", R.drawable.moto_360_black, "Moto 360, Black"),
  MOTO_360_STONE("https://play.google.com/store/devices/details/Moto_360_Stone_Leather?id=motorola_moto_360_leather_stone", R.drawable.moto_360_stone, "Moto 360, Stone"),
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
