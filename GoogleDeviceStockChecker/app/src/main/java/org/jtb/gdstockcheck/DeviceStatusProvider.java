/**
 *
 */
package org.jtb.gdstockcheck;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;

public class DeviceStatusProvider extends ContentProvider {
  // @SuppressWarnings("unused")
  private static final String TAG = "DeviceStatusProvider";

  static boolean notify = true;

  private static final int DATABASE_VERSION = 2;
  private static final String DATABASE_NAME = "gdstockcheck.db";

  private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
  private static final HashMap<String, String> DEVICE_STATUS_PROJECTION;

  private static final int DEVICE_STATUSES = 1;
  private static final int DEVICE_STATUS = 2;

  static {
    uriMatcher.addURI(DeviceStatusContract.AUTHORITY, DeviceStatusContract.DeviceStatus.CONTENT_DIRECTORY, DEVICE_STATUSES);
    uriMatcher.addURI(DeviceStatusContract.AUTHORITY, DeviceStatusContract.DeviceStatus.CONTENT_DIRECTORY + "/#", DEVICE_STATUS);

    DEVICE_STATUS_PROJECTION = new HashMap<String, String>();
    DEVICE_STATUS_PROJECTION.put(DeviceStatusContract.DeviceStatus._ID, DeviceStatusContract.DeviceStatus._ID);
    DEVICE_STATUS_PROJECTION.put(DeviceStatusContract.DeviceStatus.DEVICE, DeviceStatusContract.DeviceStatus.DEVICE);
    DEVICE_STATUS_PROJECTION.put(DeviceStatusContract.DeviceStatus.STATUS, DeviceStatusContract.DeviceStatus.STATUS);
    DEVICE_STATUS_PROJECTION.put(DeviceStatusContract.DeviceStatus.TIME, DeviceStatusContract.DeviceStatus.TIME);
  }

  private DatabaseHelper helper = null;

  private synchronized DatabaseHelper getDbHelper(Uri uri) {
    if (helper == null) {
      helper = new DatabaseHelper(getContext(), DATABASE_NAME);
    }

    return helper;
  }

  private static class DatabaseHelper extends SQLiteOpenHelper {

    DatabaseHelper(Context context, String name) {
      super(context, name, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
      String cmd = "CREATE TABLE IF NOT EXISTS " + DeviceStatusContract.DeviceStatus.CONTENT_DIRECTORY + " ("
          + DeviceStatusContract.DeviceStatus._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
          + DeviceStatusContract.DeviceStatus.DEVICE + "  TEXT NOT NULL UNIQUE,"
          + DeviceStatusContract.DeviceStatus.STATUS + " TEXT NOT NULL,"
          + DeviceStatusContract.DeviceStatus.TIME + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
          + ");";
      db.execSQL(cmd);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      Log.i(TAG, "upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
      db.execSQL("DROP TABLE IF EXISTS " + DeviceStatusContract.DeviceStatus.CONTENT_DIRECTORY);
      onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      onUpgrade(db, oldVersion, newVersion);
    }
  }

  @Override
  public int delete(Uri uri, String whereClause, String[] whereArgs) {
    DatabaseHelper dbHelper = getDbHelper(uri);
    if (dbHelper == null) {
      return 0;
    }

    SQLiteDatabase db = dbHelper.getWritableDatabase();

    int count = 0;
    long id = -1;

    switch (uriMatcher.match(uri)) {
      case DEVICE_STATUSES:
        db.beginTransaction();
        try {
          count = db.delete(DeviceStatusContract.DeviceStatus.CONTENT_DIRECTORY, whereClause, whereArgs);
          db.setTransactionSuccessful();
        } finally {
          db.endTransaction();
        }
        break;

      case DEVICE_STATUS:
        id = ContentUris.parseId(uri);
        db.beginTransaction();
        try {
          count = db.delete(DeviceStatusContract.DeviceStatus.CONTENT_DIRECTORY, DeviceStatusContract.DeviceStatus._ID + "=" + id + (!TextUtils.isEmpty(whereClause) ? " AND (" + whereClause + ")" : ""), whereArgs);
          db.setTransactionSuccessful();
        } finally {
          db.endTransaction();
        }
        break;
    }

    if (notify && count > 0) {
      getContext().getContentResolver().notifyChange(uri, null);
    }

    return count;
  }

  @Override
  public String getType(Uri uri) {
    switch (uriMatcher.match(uri)) {
      case DEVICE_STATUSES:
        return DeviceStatusContract.DeviceStatus.CONTENT_TYPE;
      case DEVICE_STATUS:
        return DeviceStatusContract.DeviceStatus.CONTENT_ITEM_TYPE;
      default:
        throw new IllegalArgumentException("unhandled URI: " + uri);
    }
  }

  @Override
  public Uri insert(Uri uri, ContentValues values) {

    DatabaseHelper dbHelper = getDbHelper(uri);
    if (dbHelper == null) {
      return null;
    }

    SQLiteDatabase db = dbHelper.getWritableDatabase();

    long rowId = 0;

    switch (uriMatcher.match(uri)) {
      case DEVICE_STATUSES:
        db.beginTransaction();
        try {
          rowId = db.insertWithOnConflict(DeviceStatusContract.DeviceStatus.CONTENT_DIRECTORY, null, values, SQLiteDatabase.CONFLICT_REPLACE);
          db.setTransactionSuccessful();
        } finally {
          db.endTransaction();
        }

        if (rowId > 0) {
          Uri rowUri = ContentUris.withAppendedId(DeviceStatusContract.DeviceStatus.CONTENT_URI, rowId);
          if (notify) {
            getContext().getContentResolver().notifyChange(rowUri, null);
          }
          return rowUri;
        }
        break;

      case DEVICE_STATUS:
        throw new IllegalArgumentException("cannot insert URI: " + uri);

      default:
        throw new IllegalArgumentException("unhandled URI: " + uri);
    }

    throw new SQLException("insert failed, URI: " + uri);
  }

  private int getAnalyticsCount(Uri uri) {
    Cursor c = null;

    try {
      c = getContext().getContentResolver().query(DeviceStatusContract.DeviceStatus.CONTENT_URI, new String[]{"count(*) AS count"}, null, null, null);
      if (c == null || !c.moveToNext()) {
        return 0;
      }
      int count = c.getInt(0);
      return count;
    } finally {
      if (c != null) {
        c.close();
      }
    }
  }

  @Override
  public boolean onCreate() {
    return true;
  }

  @Override
  public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
    SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
    long id = -1;
    String where = selection;

    DatabaseHelper dbHelper = getDbHelper(uri);
    if (dbHelper == null) {
      return null;
    }

    switch (uriMatcher.match(uri)) {
      case DEVICE_STATUSES:
        qb.setTables(DeviceStatusContract.DeviceStatus.CONTENT_DIRECTORY);
        qb.setProjectionMap(DEVICE_STATUS_PROJECTION);
        break;

      case DEVICE_STATUS:
        qb.setTables(DeviceStatusContract.DeviceStatus.CONTENT_DIRECTORY);
        qb.setProjectionMap(DEVICE_STATUS_PROJECTION);

        id = ContentUris.parseId(uri);
        if (id >= 0) {
          qb.appendWhere(DeviceStatusContract.DeviceStatus._ID + "=" + id);
        }
        break;

      default:
        throw new IllegalArgumentException("unhandled URI: " + uri);
    }

    SQLiteDatabase db = dbHelper.getWritableDatabase();
    Cursor c = qb.query(db, projection, where, selectionArgs, null, null, sortOrder);
    c.setNotificationUri(getContext().getContentResolver(), uri);

    return c;
  }

  @Override
  public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void shutdown() {
    super.shutdown();
    synchronized (this) {
      if (helper != null) {
        helper.close();
      }
    }
  }

  @Override
  public int bulkInsert(Uri uri, ContentValues[] values){
    int numInserted = 0;
    String table;

    int uriType = uriMatcher.match(uri);

    switch (uriType) {
      case DEVICE_STATUSES:
        break;
      case DEVICE_STATUS:
        throw new IllegalArgumentException("unhandled URI: " + uri);

    }
    SQLiteDatabase db = getDbHelper(uri).getWritableDatabase();
    db.beginTransaction();
    try {
      for (ContentValues cv : values) {
        long newId = db.insertWithOnConflict(DeviceStatusContract.DeviceStatus.CONTENT_DIRECTORY, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        if (newId <= 0) {
          throw new SQLException("Failed to insert row into URI: " + uri);
        }
      }
      db.setTransactionSuccessful();
      getContext().getContentResolver().notifyChange(uri, null);
      numInserted = values.length;
    } finally {
      db.endTransaction();
    }

    return numInserted;
  }
}
