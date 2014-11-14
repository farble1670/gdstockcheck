package org.jtb.gdstockcheck;

import android.app.Activity;
import android.app.LoaderManager;
import android.app.NotificationManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Timer;


public class CheckStatusActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {
  static final long INTERVAL = 1 * 60 * 1000;
  private static final Handler handler = new Handler();

  private static final int LOADER_ID = 100;

  private ListView deviceList;
  private ViewGroup progressLayout;
  private DeviceStatusCursorAdapter deviceStatusCursorAdapter;
  private Timer playTimer = null;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    deviceList = (ListView) findViewById(R.id.list_device);
    deviceStatusCursorAdapter = new DeviceStatusCursorAdapter(this);
    deviceList.setAdapter(deviceStatusCursorAdapter);
    deviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor c = (Cursor) deviceStatusCursorAdapter.getItem(position);
        DeviceStatus deviceStatus = new DeviceStatus(c);
        Intent viewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(deviceStatus.device.pageUrl));
        viewIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(viewIntent);
      }
    });
    progressLayout = (ViewGroup) findViewById(R.id.progress);

    startService(new Intent(this, CheckStatusService.class));
  }

  @Override
  protected void onResume() {
    super.onResume();

    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    for (Device device: Device.values()) {
      notificationManager.cancel(device.ordinal());
    }

    getLoaderManager().restartLoader(LOADER_ID, null, this);
  }

  @Override
  protected void onPause() {
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
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    return new CursorLoader(this, DeviceStatusContract.DeviceStatus.CONTENT_URI, null, null, null, DeviceStatusContract.DeviceStatus.DEVICE + " ASC");
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    deviceStatusCursorAdapter.swapCursor(data);
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
    deviceStatusCursorAdapter.swapCursor(null);
  }
}
