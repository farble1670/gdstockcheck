package org.jtb.gdstockcheck;

enum Device {
  NEXUS_6_BLUE_32GB("https://play.google.com/store/devices/details/Nexus_6_32GB_Midnight_Blue?id=nexus_6_blue_32gb", R.drawable.nexus_6_blue, "Nexus 6, Blue, 32GB"),
  NEXUS_6_BLUE_64GB("https://play.google.com/store/devices/details/Nexus_6_64GB_Midnight_Blue?id=nexus_6_blue_64gb", R.drawable.nexus_6_blue, "Nexus 6, Blue, 64GB" ),
  NEXUS_6_WHITE_32GB("https://play.google.com/store/devices/details/Nexus_6_32GB_Cloud_White?id=nexus_6_white_32gb", R.drawable.nexus_6_white, "Nexus 6, White, 32GB"),
  NEXUS_6_WHITE_64GB("https://play.google.com/store/devices/details/Nexus_6_64GB_Cloud_White?id=nexus_6_white_64gb", R.drawable.nexus_6_white, "Nexus 6, White, 64GB"),
  //NEXUS_9_BLACK_16GB("https://play.google.com/store/devices/details/Nexus_9_16GB_Wi_Fi_Indigo_Black?id=nexus_9_black_16gb_wifi", R.drawable.nexus_9_black, "Nexus 9, Black, 16GB"),
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
