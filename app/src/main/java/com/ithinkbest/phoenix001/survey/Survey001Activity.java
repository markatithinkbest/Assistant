package com.ithinkbest.phoenix001.survey;

import android.content.Intent;
import android.database.Cursor;
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
import com.ithinkbest.phoenix001.R;
import com.ithinkbest.phoenix001.SurveyProvider;

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

;


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

    Cursor resultCursor;
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
        // to fix
        // bug: after survey, when check result it shows last question
        questionNumber = 0;
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
        if (working_mode == CHECKING_SURVEY_RESULT) {
            //


            //
            btnSubmit.setVisibility(View.VISIBLE);
            btnSubmit.setEnabled(false);

            btnSubmit.setText("查看調研結果,隨時可以以手機的返回鍵回到上一層畫面");

            resultCursor = getQuestionResultCursor(QUESTION_ID);
            Log.d(LOG_TAG, "CURSOR CNT=>" + resultCursor.getCount());
        }
        ShowQuestion();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // GOOD PRACTICE
        //http://developer.android.com/reference/android/database/Cursor.html
        //Caused by: java.lang.NullPointerException
        if (resultCursor != null) {
            resultCursor.close();
        } else {
            Log.d(LOG_TAG, "It seems resultCursor is null!");
        }
    }

    private Cursor getQuestionResultCursor(String question_id) {
        Log.d(LOG_TAG, " ...DOING QUERY");
        String[] projection = new String[]{
                SurveyProvider.COLUMN_QUESTION_ID,
                SurveyProvider.COLUMN_ANS01,
                SurveyProvider.COLUMN_ANS02,
                SurveyProvider.COLUMN_ANS03,
        };
        String selection = SurveyProvider.COLUMN_QUESTION_ID + "='" + question_id + "'";
        Cursor cursor = getContentResolver().query(
                SurveyProvider.CONTENT_URI,
                projection,
                selection,
                null, null);


        return cursor;
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

        if (working_mode == DOING_SURVEY) {
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

    private int[] getAnswerCount() {
        int[] valArray = {0, 0, 0, 0, 0, 0};
        Log.d(LOG_TAG, "ShowQuestion() questionNumber=" + questionNumber);
        int val = 0;
        resultCursor.moveToFirst();
        do {

            switch (questionNumber) {
                case 0: // match to ans1
                    val = Integer.parseInt(resultCursor.getString(1));
                    break;
                case 1:// match to ans2
                    val = Integer.parseInt(resultCursor.getString(2));
                    break;

                case 2:// match to ans3
                    val = Integer.parseInt(resultCursor.getString(3));
                    break;

                default:
                    Log.d(LOG_TAG, "ShowQuestion(), TODO ... questionNumber=" + questionNumber);

            }
            //
            valArray[val]++;
        } while (resultCursor.moveToNext());

        return valArray;
    }

    private void ShowQuestion() {

        checkComplete();
        if (working_mode == DOING_SURVEY) {
            txtQuestion.setText(QUESTION_SET[questionNumber][0]);
            radioBtn1.setText(QUESTION_SET[questionNumber][1]);
            radioBtn2.setText(QUESTION_SET[questionNumber][2]);
            radioBtn3.setText(QUESTION_SET[questionNumber][3]);
        }
        if (working_mode == CHECKING_SURVEY_RESULT) {
            radioBtn1.setEnabled(false);
            radioBtn2.setEnabled(false);
            radioBtn3.setEnabled(false);

            int num1 = 0;
            int num2 = 60;
            int num3 = 25;
            int[] numVal=getAnswerCount();
            num1=numVal[1];
            num2=numVal[2];
            num3=numVal[3];


//            String style="% -- "
            String style = " ";
            float total = num1 + num2 + num3;

//            String.format("%.2f", value) ;

            String strFormat = "%4.1f%% -- %s -- 計%d票 ";
            String result1 = String.format(strFormat, 100 * num1 / total, QUESTION_SET[questionNumber][1], num1);
            String result2 = String.format(strFormat, 100 * num2 / total, QUESTION_SET[questionNumber][2], num2);
            String result3 = String.format(strFormat, 100 * num3 / total, QUESTION_SET[questionNumber][3], num3);


            txtQuestion.setText(QUESTION_SET[questionNumber][0]);
            radioBtn1.setText(result1);
            radioBtn2.setText(result2);
            radioBtn3.setText(result3);
        }
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
        if (working_mode == CHECKING_SURVEY_RESULT) {
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


                    //
                    //
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
        String str = "http://ithinkbest.com/gcm/phoenix/send_survey_update.php?secure_code=123456glkj527364859fj12@3&question_id=" + QUESTION_ID;

/*

//PROBLEM
checking
http://ithinkbest.com/gcm/phoenix/send_survey_update.php?secure_code=123456glkj527364859fj12@3&question_id=PHOENIX-001

got result
"registration_ids" field cannot be empty

--- after fix
{"multicast_id":7678625375688475679,"success":1,"failure":0,"canonical_ids":0,"results":[{"message_id":"0:1430133540770088%10a18a96f9fd7ecd"}]}

        //since i clean up entire table data
        //v_survey_result is broken, now return nothing


        select `survey_result`.`reg_id` AS `reg_id`,
        `survey_result`.`question_id` AS `question_id`,
        `survey_result`.`ans01` AS `ans01`,
        `survey_result`.`ans02` AS `ans02`,
        `survey_result`.`ans03` AS `ans03`,
        `survey_result`.`ans04` AS `ans04`,
        `survey_result`.`ans05` AS `ans05`,
        max(`survey_result`.`time_stamp`) AS `latest_time`,
        count(`survey_result`.`time_stamp`) AS `submit_cnt`
        from `survey_result`
        where (`survey_result`.`question_id` = 'a123456')
        group by `survey_result`.`reg_id`

       NOT ANY SYSTEM ERROR, BUT PROGRAMMER
       AFTER TESTING,
       I DIDN'T REMOVE WHERE CLAUSE


CREATE VIEW v_survey_result_fix1 AS
   select `survey_result`.`reg_id` AS `reg_id`,
        `survey_result`.`question_id` AS `question_id`,
        `survey_result`.`ans01` AS `ans01`,
        `survey_result`.`ans02` AS `ans02`,
        `survey_result`.`ans03` AS `ans03`,
        `survey_result`.`ans04` AS `ans04`,
        `survey_result`.`ans05` AS `ans05`,
        max(`survey_result`.`time_stamp`) AS `latest_time`,
        count(`survey_result`.`time_stamp`) AS `submit_cnt`
        from `survey_result`

        group by `survey_result`.`reg_id`

        */



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
