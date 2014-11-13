package org.jtb.gdstockcheck;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

class CheckStatusTask extends AsyncTask<Void,Void, org.jtb.gdstockcheck.Status> implements StatusListener {
  private final Context context;
  private final Device device;
  private final View itemView;

  CheckStatusTask(Context context, Device device, View itemView) {
    this.context = context;
    this.device = device;
    this.itemView = itemView;
  }

  @Override
  protected void onPreExecute() {
    ViewGroup progressLayout = (ViewGroup) itemView.findViewById(R.id.progress);
    progressLayout.setVisibility(View.VISIBLE);
  }

  @Override
  protected org.jtb.gdstockcheck.Status doInBackground(Void... params) {
    ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo info = connManager.getActiveNetworkInfo();
    if (!info.isConnected()) {
      return org.jtb.gdstockcheck.Status.UNKNOWN;
    }

    return new CheckStatus(device).call();
  }

  @Override
  protected void onPostExecute(org.jtb.gdstockcheck.Status s) {
    onStatus(device, s);

    if (!itemView.getTag().equals(device)) {
      return;
    }

    TextView statusText = (TextView) itemView.findViewById(R.id.status);
    statusText.setText(s.name());
    statusText.setTextColor(s.color);

    ViewGroup progressLayout = (ViewGroup) itemView.findViewById(R.id.progress);
    progressLayout.setVisibility(View.GONE);
  }

  @Override
  public void onStatus(Device device, org.jtb.gdstockcheck.Status status) {
  }
}
