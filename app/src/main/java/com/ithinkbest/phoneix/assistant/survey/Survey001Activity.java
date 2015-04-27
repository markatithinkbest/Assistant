package com.ithinkbest.phoneix.assistant.survey;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.ithinkbest.phoneix.assistant.R;

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


public class Survey001Activity extends ActionBarActivity {
    static String LOG_TAG = "MARK987 S1Activity ";


    static int working_mode;// SURVEY OR CHECK RESULT
    static int DOING_SURVEY = 1;// SURVEY OR CHECK RESULT
    static int CHECKING_SURVEY_RESULT = 2;// SURVEY OR CHECK RESULT


    // public final static String EXTRA_MESSAGE = "Survey001Activity.MESSAGE";
    //    Phoneix Assistant, oops, typo of phoenix
//    Project ID: turnkey-env-92723 Project Number: 525555782914
    String PROJECT_NUMBER = "525555782914";
    static String regid = null;
    GoogleCloudMessaging gcm;

    public static final String QUESTION_ID = "PHOENIX-001";
    static int questionNumber = 0;
    static int totalQuestion = 3;


    TextView txtQuestion;
    RadioGroup radioGrp;
    RadioButton radioBtn1;
    RadioButton radioBtn2;
    RadioButton radioBtn3;
    Button btnPrev;
    Button btnNext;
    Button btnSubmit;

    //    微型創業鳳凰> 創業課程> 創業入門班
//    beboss.wda.gov.tw/cht/index.php?code=list&ids=6
//    整年度創業課程查詢, 我要報名此課程, 創業入門班, 創業進階班,
    public static final String[][] QUESTION_SET = new String[][]{

            {"1. 上了鳳凰創業進階班,增加我對創業的必要知識,", "沒什麼幫助", "有幫助", "很有幫助"},
            {"2. 進階班課程,對我申請貸款,", "沒什麼幫助", "有幫助", "很有幫助"},
            {"3. 我會不會介紹有需要的朋友參加這個課程,", "不會", "不一定", "肯定會"},


    };
    // WARNING: need to maintain with QUESTION_SET together
    static int[] answer = {-1, -1, -1, -1, -1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey001);

        // savedInstanceState.
        Intent intent = getIntent();
        String msg = intent.getStringExtra(Common.EXTRA_MESSAGE);

        Log.d(LOG_TAG, " ...  msg => " + msg);
        if (msg.equals(Common.MODE_SURVEY)) {
            working_mode = DOING_SURVEY;
        } else {
            working_mode = CHECKING_SURVEY_RESULT;
        }
        Log.d(LOG_TAG, " ...  working_mode => " + working_mode);

        txtQuestion = (TextView) findViewById(R.id.txtQuestion);
        radioGrp = (RadioGroup) findViewById(R.id.radioGrp);

        radioBtn1 = (RadioButton) findViewById(R.id.radioBtn1);
        radioBtn2 = (RadioButton) findViewById(R.id.radioBtn2);
        radioBtn3 = (RadioButton) findViewById(R.id.radioBtn3);
        btnPrev = (Button) findViewById(R.id.btnPrev);
        btnNext = (Button) findViewById(R.id.btnNext);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        if (working_mode==CHECKING_SURVEY_RESULT) {
            btnSubmit.setVisibility(View.VISIBLE);
            btnSubmit.setEnabled(false);

            btnSubmit.setText("查看調研結果,隨時可以以手機的返回鍵回到上一層畫面");
        }
        ShowQuestion();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_s1, menu);
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

    private void handleButtons() {
        // when first question, no prev
        if (questionNumber == 0) {
            btnPrev.setEnabled(false);
        } else {
            btnPrev.setEnabled(true);
        }

        // when last question, no next
        if (questionNumber == totalQuestion - 1) {
            btnNext.setEnabled(false);
        } else {
            btnNext.setEnabled(true);
        }

        if (working_mode==DOING_SURVEY) {
            if (answer[questionNumber] == -1) {
                // when entering new or unanswered question,
                radioGrp.clearCheck();
            } else {
                // when revisit answered question
                switch (answer[questionNumber]) {
                    case 1:
                        radioGrp.check(radioBtn1.getId());
                        break;
                    case 2:
                        radioGrp.check(radioBtn2.getId());
                        break;
                    case 3:
                        radioGrp.check(radioBtn3.getId());
                        break;
                }
            }
        }
    }

    public void onPrevButtonClicked(View view) {
        questionNumber--;

        ShowQuestion();

    }

    public void onNextButtonClicked(View view) {
        questionNumber++;

        ShowQuestion();
    }

    private void ShowQuestion() {

        checkComplete();
        txtQuestion.setText(QUESTION_SET[questionNumber][0]);
        radioBtn1.setText(QUESTION_SET[questionNumber][1]);
        radioBtn2.setText(QUESTION_SET[questionNumber][2]);
        radioBtn3.setText(QUESTION_SET[questionNumber][3]);

        handleButtons();
    }

    public void onSubmitButtonClicked(View view) {
        Log.d(LOG_TAG, "...SUBMIT");
        getRegId();

    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radioBtn1:
                if (checked) {
                    answer[questionNumber] = 1;

                    //issue#1
//                    lastChecked = answer[questionNumber];
//                    Log.d(LOG_TAG, "...lastChecked= " + lastChecked);
//                    Log.d(LOG_TAG, "...radioButton1 " + Arrays.toString(answer));

                }
                // Pirates are the best
                break;
            case R.id.radioBtn2:
                if (checked) {
                    answer[questionNumber] = 2;

                    //issue#1
//                    lastChecked = answer[questionNumber];
//                    Log.d(LOG_TAG, "...lastChecked= " + lastChecked);
//                    Log.d(LOG_TAG, "...radioButton2 " + Arrays.toString(answer));

                }
                // Ninjas rule
                break;
            case R.id.radioBtn3:
                if (checked) {
                    answer[questionNumber] = 3;

                    //issue#1
//                    lastChecked = answer[questionNumber];
//                    Log.d(LOG_TAG, "...lastChecked= " + lastChecked);
//                    Log.d(LOG_TAG, "...radioButton3 " + Arrays.toString(answer));
                }
                // Ninjas rule
                break;
        }
        checkComplete();
    }

    private void checkComplete() {
        if (working_mode==CHECKING_SURVEY_RESULT) {
            btnSubmit.setVisibility(View.VISIBLE);
            btnSubmit.setEnabled(false);

            btnSubmit.setText("查看調研結果,隨時可以以手機的返回鍵回到上一層畫面");
            return;
        }
//        boolean isComplete=true;
        int answeredCnt = 0;
        for (int i = 0; i < QUESTION_SET.length; i++) {
            if (answer[i] == -1) {
//                isComplete=false;
//                break;
            } else {
                answeredCnt++;
            }
        }
//        Log.d(LOG_TAG, "...answer??? "+answeredCnt+"  " + Arrays.toString(answer));
        if (answeredCnt == totalQuestion) {
            btnSubmit.setText(getString(R.string.submit));
            btnSubmit.setEnabled(true);
        } else {
            btnSubmit.setText("已作答" + answeredCnt + "題, 共" + totalQuestion + "題");
        }
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
//                    Log.i(LOG_TAG, "... to submit survey result");
//                    Log.i(LOG_TAG, msg);

                    String result = readGcmInsertResult();
//                    Log.i(LOG_TAG, "...readGcmInsertResult() "+result);
                    msg = result.split("<BR>")[1].trim(); // extra space after <BR>1#
                    Log.i(LOG_TAG, msg);


                } catch (IOException ex) {
//                    msg = "Error :" + ex.getMessage();
                    msg = "-1";

                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
//                Log.i(LOG_TAG, "...onPostExecute, msg="+msg);
//                int intMsg=Integer.parseInt(msg);
//                Log.i(LOG_TAG, "...onPostExecute, int msg="+msg);

                if (msg.equals("1")) {
//                    Toast.makeText(getApplicationContext(), "成功提交送出到雲端服務", Toast.LENGTH_LONG).show();
                    btnSubmit.setEnabled(false);
                    btnPrev.setEnabled(false);
                    btnSubmit.setText("提交到雲端服務成功,請按手機的返回,回到上一層畫面.");
                    Log.d(LOG_TAG, "...triggerGcm();");
                    triggerGcm();

                } else {
                    Toast.makeText(getApplicationContext(), "沒有能夠成功提交送出到雲端服務,請檢查您手機的網路連線,或稍後再試.", Toast.LENGTH_LONG).show();

                }
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

        String ans01 = "" + answer[0];
        String ans02 = "" + answer[1];
        String ans03 = "" + answer[2];
        String ans04 = "" + answer[3];
        String ans05 = "" + answer[4];

        StringBuilder surveyResult = new StringBuilder();
        surveyResult.append("&question_id=" + QUESTION_ID)
                .append("&ans01=" + ans01)
                .append("&ans02=" + ans02)
                .append("&ans03=" + ans03)
                .append("&ans04=" + ans04)
                .append("&ans05=" + ans05);

//        String str = "http://ithinkbest.com/gcm/phoenix/gcm_insert.php?reg_id=" + regid+surveyResult.toString();
        String str = "http://ithinkbest.com/gcm/phoenix/submit_survey.php?reg_id=" + regid + surveyResult.toString();

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


    public String triggerGcmCore() {
        HttpClient client = new DefaultHttpClient();
        StringBuilder builder = new StringBuilder();
//        String str = "http://ithinkbest.com/gcm/phoenix/gcm_insert.php?reg_id=" + regid+surveyResult.toString();
//        String str = "http://ithinkbest.com/gcm/phoenix/submit_survey.php?reg_id=" + regid+surveyResult.toString();
        String str = "http://ithinkbest.com/gcm/phoenix/send_survey_update.php?secure_code=123456glkj527364859fj12@3&question_id=" + QUESTION_ID;

        HttpGet httpGet = new HttpGet(str);
        Log.d(LOG_TAG, "... TO INFORM SERVER TO  => " + str);
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

    public void triggerGcm() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String result = triggerGcmCore();
                //   String msg=    result.split("<BR>")[1].trim(); // extra space after <BR>1#

                return result;
            }

            @Override
            protected void onPostExecute(String msg) {
                Log.i(LOG_TAG, "...onPostExecute, msg=" + msg);
            }
        }.execute(null, null, null);
    }
    // === GCM Util === end
}
