package com.ithinkbest.phoneix.assistant;

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


public class S1Activity extends ActionBarActivity {
    static String LOG_TAG = "MARK987 S1Activity ";
    static int questionNumber = 0;
    static int totalQuestion = 4;
    static int lastChecked = -1;


    TextView txtQuestion;
    RadioGroup radioGrp;
    RadioButton radioBtn1;
    RadioButton radioBtn2;
    RadioButton radioBtn3;
    Button btnPrev;
    Button btnNext;
    Button btnSubmit;



    static final String[] QUESTION = new String[]{
            "000",
            "111您使用Anroid手機,已經多久了?",

            "您使用Anroid手機,已經多久了?",
            "3. 請您自我評估使用Anroid手機的熟練度?"

    };
    //    <string name="s1_01">1. 操作這支APP您覺得?</string>
//    <string name="s1_01a">容易</string>
//    <string name="s1_01b">有點困難</string>
//    <string name="s1_01c">很困難</string>
//    radioBtn1.setText("三個月以內");
//    radioBtn2.setText("三個月到一年左右");
//    radioBtn3.setText("一年以上");
//
//    radioBtn1.setText("還很陌生");
//    radioBtn2.setText("一般夠用");
//    radioBtn3.setText("非常熟練");
    static final String[][] QUESTION_SET = new String[][]{

            {"1. 操作這支APP,您個人覺得?", "容易", "有點困難", "很困難"},
            {"2. 您使用Anroid手機,已經多久了?", "三個月以內", "三個月到一年左右", "一年以上"},
            {"3. 請您自我評估,使用Anroid手機的熟練度?", "還很陌生", "一般夠用", "非常熟練"},
            {"4. 商工行政資料開放平台的內容,對你而言?", "沒有什麼用", "可以增加常識", "有參考價值"}

    };
    // WARNING: need to maintain with QUESTION_SET together
    static int[] answer = {-1, -1, -1, -1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s1);
        txtQuestion = (TextView) findViewById(R.id.txtQuestion);
        radioGrp = (RadioGroup) findViewById(R.id.radioGrp);

        radioBtn1 = (RadioButton) findViewById(R.id.radioBtn1);
        radioBtn2 = (RadioButton) findViewById(R.id.radioBtn2);
        radioBtn3 = (RadioButton) findViewById(R.id.radioBtn3);
        btnPrev = (Button) findViewById(R.id.btnPrev);
        btnNext = (Button) findViewById(R.id.btnNext);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

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
        Log.d(LOG_TAG,"...SUBMIT");

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
    private void checkComplete(){

//        boolean isComplete=true;
        int answeredCnt=0;
        for (int i=0;i<QUESTION_SET.length;i++){
            if (answer[i]==-1){
//                isComplete=false;
//                break;
            }else{
                answeredCnt++;
            }
        }
//        Log.d(LOG_TAG, "...answer??? "+answeredCnt+"  " + Arrays.toString(answer));
        if (answeredCnt==totalQuestion){
            btnSubmit.setText(getString(R.string.submit));
            btnSubmit.setEnabled(true);
        }else{
            btnSubmit.setText("已作答"+answeredCnt+"題, 共"+totalQuestion+ "題");
        }
    }
}
