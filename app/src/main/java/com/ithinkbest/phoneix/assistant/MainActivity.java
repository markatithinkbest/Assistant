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
import com.ithinkbest.phoneix.assistant.opendata.OpenDataActivity;
import com.ithinkbest.phoneix.assistant.survey.CheckSurveyResultActivity;
import com.ithinkbest.phoneix.assistant.survey.SurveyActivity;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;


public class MainActivity extends ActionBarActivity {
    static String LOG_TAG = "MARK987 MainActivity";

    // --- GCM ---
   // String PROJECT_NUMBER = "538682377549";// Project ID: taipei-ok Project Number: 538682377549

//    Phoneix Assistant, oops, typo of phoenix
//    Project ID: turnkey-env-92723 Project Number: 525555782914
    String PROJECT_NUMBER = "525555782914";
    static String regid = null;
    GoogleCloudMessaging gcm;




    public void syncDb() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String urlString = "http://www.ithinkbest.com/gcm/phoenix/get_survey.php?security_code=abc123";
                String strData = null;

                Vector<ContentValues> cVVector = null;

                try {
                    InputStream inputStream = downloadUrl(urlString);//
                    strData = IOUtils.toString(inputStream);
                    Log.d(LOG_TAG, "...check raw json " + strData);
                    JSONArray jsonArray=new JSONArray(strData);
                    cVVector = new Vector<ContentValues>(jsonArray.length());
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
                        cVVector.add(mNewValues);

//                        Uri mNewUri;
//                        mNewUri = getContentResolver().insert(
//                                SurveyProvider.CONTENT_URI,   // the user dictionary content URI
//                                mNewValues                          // the values to insert
//                        );
//                        Log.d(LOG_TAG, " mNewUri="+mNewUri.getPath());

                    }



                } catch (IOException e) {
                    Log.d(LOG_TAG, " ###syncDb IOException "+e.toString());
                    e.printStackTrace();
                } catch (JSONException e) {
                    Log.d(LOG_TAG, " ###syncDb JSONException "+e.toString());
                    e.printStackTrace();
                }
                if ( cVVector.size() > 0 ) {
                    String str = null;

                    //  String selection = TaipeiOkProvider.COLUMN_CERTIFICATION_CATEGORY+"=\""+ TaipeiOkProvider.CATXX[cat]+"\"" ;

                    int delCnt = getContentResolver().delete(SurveyProvider.CONTENT_URI,
                            null,
                            null);
                    Log.d(LOG_TAG, "del cnt= " + delCnt);


                    ContentValues[] cvArray = new ContentValues[cVVector.size()];
                    cVVector.toArray(cvArray);
                    int bulkCnt = getContentResolver().bulkInsert(SurveyProvider.CONTENT_URI, cvArray);
                    Log.d(LOG_TAG, "bulk cnt= " + bulkCnt);
                }
              //  Log.d(LOG_TAG, "...doInBackground ...GOING TO DO CONTENT PROVIDER");
                return "###TODO content provider ";
            }

            @Override
            protected void onPostExecute(String msg) {
                Log.d(LOG_TAG, "...doing syncDb onPostExecute(String msg) =>"+msg);
                Log.d(LOG_TAG, "... AFTER DATA READY,  notifyGcm();");

             //   notifyGcm();
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      //  getRegId();
        syncDb();


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
        Intent intent = new Intent(this, CheckSurveyResultActivity.class);

        startActivity(intent);
    }
    public void onClickBtn4(View view) {
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
