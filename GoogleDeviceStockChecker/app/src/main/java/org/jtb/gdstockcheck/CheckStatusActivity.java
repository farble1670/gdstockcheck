package org.jtb.gdstockcheck;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


public class CheckStatusActivity extends Activity implements StatusListener {
  static final long INTERVAL = 1 * 60 * 1000;
  private static final Handler handler = new Handler();

  private ListView deviceList;
  private ViewGroup progressLayout;
  private DeviceAdapter deviceAdapter;
  private Runnable refreshRunnable = new Runnable() {
    @Override
    public void run() {
      deviceAdapter.notifyDataSetChanged();
      handler.postDelayed(this, INTERVAL);
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    deviceList = (ListView) findViewById(R.id.list_device);
    deviceAdapter = new DeviceAdapter(this, Device.values());
    deviceList.setAdapter(deviceAdapter);
    deviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Device device = (Device) deviceAdapter.getItem(position);
        Intent viewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(device.pageUrl));
        viewIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(viewIntent);
      }
    });
    progressLayout = (ViewGroup) findViewById(R.id.progress);

    CheckStatusReceiver.schedule(this);
  }

  @Override
  protected void onResume() {
    super.onResume();

    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    for (Device device: Device.values()) {
      notificationManager.cancel(device.ordinal());
    }
    refreshRunnable.run();
  }

  @Override
  protected void onPause() {
    handler.removeCallbacks(refreshRunnable);
    super.onPause();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    int id = item.getItemId();

    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onStatus(Device device, Status status) {
    if (status == Status.IN_STOCK) {
      new Alert(this).play();
    }
  }
}
