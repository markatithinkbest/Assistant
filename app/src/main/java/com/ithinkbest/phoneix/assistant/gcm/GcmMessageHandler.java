package com.ithinkbest.phoneix.assistant.gcm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.ithinkbest.phoneix.assistant.R;
import com.ithinkbest.phoneix.assistant.SurveyProvider;
import com.ithinkbest.phoneix.assistant.ToGcmActivity;
import com.ithinkbest.phoneix.assistant.ToGooglePlayActivity;
import com.ithinkbest.phoneix.assistant.gcm.GcmBroadcastReceiver;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GcmMessageHandler extends IntentService {
    static String LOG_TAG = "MARK987 GcmMessageHandler";
    String mes;
    private Handler handler;

    public GcmMessageHandler() {
        super("GcmMessageHandler");
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        handler = new Handler();
    }

    @Override
    protected void onHandleIntent(Intent intent) {


        Bundle extras = intent.getExtras();

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

//        mes = extras.getString("title");
        mes = extras.getString("message");

        showToast();
        Log.i(LOG_TAG, "Received : (" + messageType + ")  " + extras.getString("message"));

        GcmBroadcastReceiver.completeWakefulIntent(intent);

    }

    private void notifyGcm() {
        int idGooglePlay = 12347;
        String shortMsg = mes;
        if (shortMsg.length() > 24) {
            shortMsg = shortMsg.substring(0, 24) + " ...";
        }

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText("Received: " + mes);
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, ToGcmActivity.class)
                .putExtra("message", mes);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(ToGooglePlayActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(idGooglePlay, mBuilder.build());

    }

    public void doAsyncTask() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {

                return "xxx";
            }

            @Override
            protected void onPostExecute(String msg) {
            }
        }.execute(null, null, null);
    }

    public void syncDb() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String urlString = "http://www.ithinkbest.com/gcm/phoenix/get_survey.php?security_code=abc123";
                String strData = null;
                try {
                    InputStream inputStream = downloadUrl(urlString);//
                    strData = IOUtils.toString(inputStream);
                    Log.d(LOG_TAG, "...check raw json " + strData);
                    JSONArray jsonArray=new JSONArray(strData);
                    JSONObject jsonObject=null;
                    for (int i=0;i<jsonArray.length();i++){
                        jsonObject=jsonArray.getJSONObject(i);
                        //cloud_id":"92","reg_id_crc32":"2741685047","question_id":"","ans01":"","ans02":"","ans03":"","ans04":"","an
                        String cloud_id= jsonObject.getString(SurveyProvider.COLUMN_CLOUD_ID);
                        int int_cloud_id=Integer.parseInt(cloud_id);
                        String reg_id_crc32= jsonObject.getString(SurveyProvider.COLUMN_REG_ID_CRC32);
                        String question_id= jsonObject.getString(SurveyProvider.COLUMN_QUESTION_ID);
                        String ans01= jsonObject.getString(SurveyProvider.COLUMN_ANS01);
                        String ans02= jsonObject.getString(SurveyProvider.COLUMN_ANS02);
                        String ans03= jsonObject.getString(SurveyProvider.COLUMN_ANS03);
                        String ans04= jsonObject.getString(SurveyProvider.COLUMN_ANS04);
                        String ans05= jsonObject.getString(SurveyProvider.COLUMN_ANS05);

// TESTING CONTENT PROVIDER
                        ContentValues mNewValues = new ContentValues();

/*
 * Sets the values of each column and inserts the word. The arguments to the "put"
 * method are "column name" and "value"
 */
                        mNewValues.put(SurveyProvider.COLUMN_CLOUD_ID, int_cloud_id);
                        mNewValues.put(SurveyProvider.COLUMN_QUESTION_ID, question_id);
                        mNewValues.put(SurveyProvider.COLUMN_REG_ID_CRC32,reg_id_crc32);
                        mNewValues.put(SurveyProvider.COLUMN_ANS01, ans01);
                        mNewValues.put(SurveyProvider.COLUMN_ANS02, ans02);
                        mNewValues.put(SurveyProvider.COLUMN_ANS03, ans03);
                        mNewValues.put(SurveyProvider.COLUMN_ANS04, ans04);
                        mNewValues.put(SurveyProvider.COLUMN_ANS05, ans05);


                        Uri mNewUri;
                        mNewUri = getContentResolver().insert(
                                SurveyProvider.CONTENT_URI,   // the user dictionary content URI
                                mNewValues                          // the values to insert
                        );
                        Log.d(LOG_TAG, " mNewUri="+mNewUri.getPath());

                    }



                } catch (IOException e) {
                    Log.d(LOG_TAG, " ###syncDb IOException "+e.toString());
                    e.printStackTrace();
                } catch (JSONException e) {
                    Log.d(LOG_TAG, " ###syncDb JSONException "+e.toString());
                    e.printStackTrace();
                }

                Log.d(LOG_TAG, "...doInBackground ...GOING TO DO CONTENT PROVIDER");
                return "###TODO content provider ";
            }

            @Override
            protected void onPostExecute(String msg) {
                Log.d(LOG_TAG, "...doing syncDb onPostExecute(String msg) =>"+msg);
                Log.d(LOG_TAG, "... AFTER DATA READY,  notifyGcm();");

                notifyGcm();
            }
        }.execute(null, null, null);

    }

    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        return conn.getInputStream();
    }

    public void showToast() {
        handler.post(new Runnable() {
            public void run() {
                //    Toast.makeText(getApplicationContext(), mes, Toast.LENGTH_LONG).show();
                syncDb();
              //  notifyGcm();

            }
        });

    }
}
