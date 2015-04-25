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

import java.util.Arrays;


public class S1Activity extends ActionBarActivity {
    static String LOG_TAG = "MARK987";
    static int questionNumber = 0;
    static int totalQuestion = 3;


    TextView txtQuestion;
    RadioGroup radioGrp;
    RadioButton radioBtn1;
    RadioButton radioBtn2;
    RadioButton radioBtn3;
    Button btnPrev;
    Button btnNext;


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
            {"3. 請您自我評估,使用Anroid手機的熟練度?", "還很陌生", "一般夠用", "非常熟練"}

    };
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
        if (questionNumber == 0) {
            btnPrev.setEnabled(false);
        } else {
            btnPrev.setEnabled(true);
        }

        if (questionNumber == totalQuestion - 1) {
            btnNext.setEnabled(false);
        } else {
            btnNext.setEnabled(true);
        }

        if (answer[questionNumber] == -1) {
            radioBtn1.setChecked(false);
            radioBtn2.setChecked(false);
            radioBtn3.setChecked(false);
            Log.d(LOG_TAG, "SHOULD BE NONE CHECKED questionNumber=" + questionNumber + " answer[questionNumber]=" + answer[questionNumber]);

        } else {
            radioBtn1.setChecked(true);
            radioBtn2.setChecked(true);
            radioBtn3.setChecked(true);
            Log.d(LOG_TAG, "questionNumber=" + questionNumber + " answer[questionNumber]=" + answer[questionNumber]);
            switch (answer[questionNumber]) {
                case 1:
                    radioGrp.check(radioBtn1.getId());
                    Log.d(LOG_TAG, "CASE 1 setChecked");
                    break;
                case 2:
                    radioGrp.check(radioBtn2.getId());
                    Log.d(LOG_TAG, "CASE 2 setChecked");
                    break;
                case 3:
                    radioGrp.check(radioBtn3.getId());
                    Log.d(LOG_TAG, "CASE 3 setChecked");
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
        handleButtons();
        txtQuestion.setText(QUESTION_SET[questionNumber][0]);
        radioBtn1.setText(QUESTION_SET[questionNumber][1]);
        radioBtn2.setText(QUESTION_SET[questionNumber][2]);
        radioBtn3.setText(QUESTION_SET[questionNumber][3]);

        switch (questionNumber) {
            case 2:
//                txtQuestion.setText(QUESTION[2]);
//                radioBtn1.setText("三個月以內");
//                radioBtn2.setText("三個月到一年左右");
//                radioBtn3.setText("一年以上");
                break;
            case 3:
//                txtQuestion.setText(QUESTION[3]);
//                radioBtn1.setText("還很陌生");
//                radioBtn2.setText("一般夠用");
//                radioBtn3.setText("非常熟練");
                break;

        }
    }


    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radioBtn1:
                if (checked) {
                    answer[questionNumber] = 1;
                    Log.d(LOG_TAG, "...radioButton1 " + Arrays.toString(answer));

                }
                // Pirates are the best
                break;
            case R.id.radioBtn2:
                if (checked) {
                    answer[questionNumber] = 2;
                    Log.d(LOG_TAG, "...radioButton2 " + Arrays.toString(answer));

                }
                // Ninjas rule
                break;
            case R.id.radioBtn3:
                if (checked) {
                    answer[questionNumber] = 3;
                    Log.d(LOG_TAG, "...radioButton3 " + Arrays.toString(answer));
                }
                // Ninjas rule
                break;
        }
    }
}
