package com.ithinkbest.phoenix001;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;

public class SurveyProvider extends ContentProvider {
    static String LOG_TAG = "MARK987";

    static final String PROVIDER_NAME = "com.ithinkbest.phoenix001.SurveyProvider";
//    static final String PROVIDER_NAME = "com.ithinkbest.phoenix001.SurveyProvider";
                                        //"com.ithinkbest.phoneix.assistant
    private static final String SUB1 = "sub1";
    private static final String SUB2 = "sub2"; // for rawQuery

    private static final String URL = "content://" + PROVIDER_NAME + "/" + SUB1;
    private static final String URL_RAW_QUERY = "content://" + PROVIDER_NAME + "/" + SUB2;

    public static final Uri CONTENT_URI = Uri.parse(URL);
    public static final Uri CONTENT_URI_RAW_QUERY = Uri.parse(URL_RAW_QUERY);

    static final int uriCode = 1;
    static final int uriCodeRawQuery = 2;


    private static HashMap<String, String> values;

    // Used to match uris with Content Providers
    static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, SUB1, uriCode);
        uriMatcher.addURI(PROVIDER_NAME, SUB2, uriCodeRawQuery);

    }

    private SQLiteDatabase sqlDB;

    DatabaseHelper dbHelper = null;

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CLOUD_ID = "cloud_id";
    public static final String COLUMN_REG_ID_CRC32 = "reg_id_crc32";
    public static final String COLUMN_QUESTION_ID = "question_id";
    public static final String COLUMN_ANS01 = "ans01";
    public static final String COLUMN_ANS02 = "ans02";
    public static final String COLUMN_ANS03 = "ans03";
    public static final String COLUMN_ANS04 = "ans04";
    public static final String COLUMN_ANS05 = "ans05";

    static private final String DATABASE_NAME = "phoenix.db"; // YOUR DESIRED DATABASE
    static private final String TABLE_NAME = "survey"; // YOUR DESIRED TABLE
    static private final int DATABASE_VERSION = 3; // ### need to increase when change

    static private final String COL0 = COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT";
    static private final String COL1 = COLUMN_CLOUD_ID + " INTEGER";
    static private final String COL2 = COLUMN_QUESTION_ID + " TEXT NOT NULL ";
    static private final String COL3 = COLUMN_REG_ID_CRC32 + " TEXT NOT NULL ";
    static private final String COL4 = COLUMN_ANS01 + " TEXT NOT NULL ";
    static private final String COL5 = COLUMN_ANS02 + " TEXT NOT NULL ";
    static private final String COL6 = COLUMN_ANS03 + " TEXT NOT NULL ";
    static private final String COL7 = COLUMN_ANS04 + " TEXT NOT NULL ";
    static private final String COL8 = COLUMN_ANS05 + " TEXT NOT NULL ";

    // table structure
    static private final String CREATE_DB_TABLE = " CREATE TABLE " + TABLE_NAME + " ("
            + COL0 + ","
            + COL1 + ","
            + COL2 + ","
            + COL3 + ","
            + COL4 + ","
            + COL5 + ","
            + COL6 + ","
            + COL7 + ","
            + COL8 + " "
            + ");";

    @Override
    public boolean onCreate() {
        dbHelper = new DatabaseHelper(getContext());
        sqlDB = dbHelper.getWritableDatabase();
        if (sqlDB != null) {
            return true;
        }
        return false;
    }


    //    @Override
    public Cursor rawQuery(String sql) {
        return dbHelper.getReadableDatabase().rawQuery(sql, null);

    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        // Used to create a SQL query
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        // Set table to query
        queryBuilder.setTables(TABLE_NAME);

        // Used to match uris with Content Providers
        switch (uriMatcher.match(uri)) {
            case uriCode:
                queryBuilder.setProjectionMap(values);
                break;
//            case uriCodeRawQuery:
//                String sql = "SELECT " + COLUMN_ID + "," +
//                        COLUMN_DISTRICT + ", COUNT(" + COLUMN_DISTRICT + ") AS CNT" +
//                        " FROM " + TABLE_NAME +
//                        " WHERE  " + selection +
//                        " GROUP BY " + COLUMN_DISTRICT;
//                Log.d(LOG_TAG, "############## raw query ############" + sql);
//                return dbHelper.getReadableDatabase().rawQuery(sql, null);
            //  break;

                        case uriCodeRawQuery:
                String sql = "SELECT " +
                        COLUMN_QUESTION_ID + ", COUNT(" + COLUMN_ANS01 + ") AS ANS01_CNT," +
                        "COUNT(" + COLUMN_ANS02 + ") AS ANS02_CNT"+
                         "COUNT(" + COLUMN_ANS03 + ") AS ANS03_CNT"+
                        " FROM " + TABLE_NAME +
                        " WHERE  " + selection +
                        " GROUP BY " + COLUMN_QUESTION_ID;
                Log.d(LOG_TAG, "############## raw query ############" + sql);
                return dbHelper.getReadableDatabase().rawQuery(sql, null);
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        // Cursor provides read and write access to the database
        Cursor cursor = queryBuilder.query(sqlDB, projection, selection,
                selectionArgs, null, null, sortOrder);

        // Register to watch for URI changes
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    // Handles requests for the MIME type (Type of Data) of the data at the URI
    @Override
    public String getType(Uri uri) {

        // Used to match uris with Content Providers
        switch (uriMatcher.match(uri)) {

            // vnd.android.cursor.dir/cpcontacts states that we expect multiple
            // pieces of data
            case uriCode:
                return "vnd.android.cursor.dir/" + SUB1;

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    // Used to insert a new row into the provider
    // Receives the URI (Uniform Resource Identifier) for the Content Provider
    // and a set of values
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        // Gets the row id after inserting a map with the keys representing the
        // the column
        // names and their values. The second att +
        // " (id INTEGER PRIMARY KEY AUTOINCREMENT, "ribute is used when you try
        // to insert
        // an empty row
        long rowID = sqlDB.insert(TABLE_NAME, null, values);

        // Verify a row has been added
        if (rowID > 0) {

            // Append the given id to the path and return a Builder used to
            // manipulate URI
            // references
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);

            // getContentResolver provides access to the content model
            // notifyChange notifies all observers that a row was updated
            getContext().getContentResolver().notifyChange(_uri, null);

            // Return the Builder used to manipulate the URI
            return _uri;
        }
        Toast.makeText(getContext(), "Row Insert Failed", Toast.LENGTH_LONG)
                .show();
        return null;
    }


    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
//        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
//        final int match = sUriMatcher.match(uri);
        sqlDB.beginTransaction();
        int returnCount = 0;
        try {
            for (ContentValues value : values) {
                //  normalizeDate(value);
                long _id = sqlDB.insert(TABLE_NAME, null, value);
                if (_id != -1) {
                    returnCount++;
                }
            }
            sqlDB.setTransactionSuccessful();
        } finally {
            sqlDB.endTransaction();
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnCount;

    }

    // Deletes a row or a selection of rows
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int rowsDeleted = 0;

        // Used to match uris with Content Providers
        switch (uriMatcher.match(uri)) {
            case uriCode:
                rowsDeleted = sqlDB.delete(TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        // getContentResolver provides access to the content model
        // notifyChange notifies all observers that a row was updated
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    // Used to update a row or a selection of rows
    // Returns to number of rows updated
    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int rowsUpdated = 0;

        // Used to match uris with Content Providers
        switch (uriMatcher.match(uri)) {
            case uriCode:

                // Update the row or rows of data
                rowsUpdated = sqlDB.update(TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        // getContentResolver provides access to the content model
        // notifyChange notifies all observers that a row was updated
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    // Creates and manages our database
    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqlDB) {
            sqlDB.execSQL(CREATE_DB_TABLE);
        }

        // Recreates the table when the database needs to be upgraded
        @Override
        public void onUpgrade(SQLiteDatabase sqlDB, int oldVersion,
                              int newVersion) {
            sqlDB.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(sqlDB);
        }
    }

}
