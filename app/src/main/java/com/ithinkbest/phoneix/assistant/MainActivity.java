package com.ithinkbest.phoneix.assistant;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class MainActivity extends ActionBarActivity {
    static String LOG_TAG = "MARK987 MainActivity";

    // --- GCM ---
   // String PROJECT_NUMBER = "538682377549";// Project ID: taipei-ok Project Number: 538682377549

//    Phoneix Assistant, oops, typo of phoenix
//    Project ID: turnkey-env-92723 Project Number: 525555782914
    String PROJECT_NUMBER = "525555782914";
    static String regid = null;
    GoogleCloudMessaging gcm;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      //  getRegId();

        // TESTING CONTENT PROVIDER
        ContentValues mNewValues = new ContentValues();

/*
 * Sets the values of each column and inserts the word. The arguments to the "put"
 * method are "column name" and "value"
 */
        mNewValues.put(SurveyProvider.COLUMN_CLOUD_ID, "111");
        mNewValues.put(SurveyProvider.COLUMN_QUESTION_ID, "222");
        mNewValues.put(SurveyProvider.COLUMN_REG_ID_CRC32, "333");
        mNewValues.put(SurveyProvider.COLUMN_ANS01, "444");
        mNewValues.put(SurveyProvider.COLUMN_ANS02, "555");
        mNewValues.put(SurveyProvider.COLUMN_ANS03, "666");
        mNewValues.put(SurveyProvider.COLUMN_ANS04, "777");
        mNewValues.put(SurveyProvider.COLUMN_ANS05, "888");


        Uri mNewUri;
        mNewUri = getContentResolver().insert(
                SurveyProvider.CONTENT_URI,   // the user dictionary content URI
                mNewValues                          // the values to insert
        );
        Log.d(LOG_TAG, " mNewUri="+mNewUri.getPath());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickBtn1(View view) {
        Log.d(LOG_TAG, "...btn1");
        Intent intent = new Intent(this, OpenDataActivity.class);
//        EditText editText = (EditText) findViewById(R.id.edit_message);
//        String message = editText.getText().toString();
//        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);


    }

    public void onClickBtn2(View view) {
//        Toast.makeText(this, "...DOING", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, SurveyActivity.class);

        startActivity(intent);
    }

    public void onClickBtn3(View view) {
//        Log.d(LOG_TAG, "...btn3");
//        Toast.makeText(this, "...DOING", Toast.LENGTH_SHORT).show();
        Uri uriUrl = Uri.parse("http://www.ithinkbest.com/?p=2365");
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }


    // === GCM Util === start
    public void getRegId() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    regid = gcm.register(PROJECT_NUMBER);
                    msg = "Device registered, registration ID=" + regid;
                    //      Toast.makeText(getApplicationContext(), "One time only, to send registration ID to App server, "+regid,Toast.LENGTH_SHORT).show();
                    Log.i(LOG_TAG, msg);

                    String result=readGcmInsertResult();
                    Log.i(LOG_TAG, "...readGcmInsertResult() "+result);


                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();

                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {

                //
            }
        }.execute(null, null, null);
    }

    public String readGcmInsertResult() {
        if (regid == null) {
            Log.d(LOG_TAG, "regid is null");
            return "";
        }
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
//        HttpGet httpGet = new HttpGet("https://bugzilla.mozilla.org/rest/bug?assigned_to=lhenry@mozilla.com");
//        String str = "http://ithinkbest.com/taipeiokgcm/gcm_insert.php?reg_id=" + regid;
        String str = "http://ithinkbest.com/gcm/phoenix/gcm_insert.php?reg_id=" + regid;

//        String str= TaipeiOkProvider.JSNXX[cat];


        HttpGet httpGet = new HttpGet(str);
        Log.d(LOG_TAG, "new HttpGet(str) => " + str);
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            } else {
                Log.e(LOG_TAG, "Failed to download file");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {

            Log.d(LOG_TAG, "Exception " + e.toString());

        }
        return builder.toString();
    }
    // === GCM Util === end

}
