package com.ithinkbest.phoneix.assistant;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;


public class S1Activity extends ActionBarActivity {
    static String LOG_TAG = "MARK987";
    static int questionNumber=1;

    TextView txtQuestion;
    RadioButton radioBtn1;
    RadioButton radioBtn2;
    RadioButton radioBtn3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s1);
        txtQuestion=(TextView)findViewById(R.id.txtQuestion) ;
        radioBtn1 =(RadioButton)findViewById(R.id.radioBtn1);
        radioBtn2 =(RadioButton)findViewById(R.id.radioBtn2);
        radioBtn3 =(RadioButton)findViewById(R.id.radioBtn3);

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

    public void onPrevButtonClicked(View view) {
        questionNumber--;
        ShowQuestion();

    }
    public void onNextButtonClicked(View view) {
        questionNumber++;
        ShowQuestion();
    }

    private void ShowQuestion(){
        switch (questionNumber){
            case 2:
                txtQuestion.setText("2. 您使用Anroid手機,已經多久了?");
                radioBtn1.setText("三個月以內");
                radioBtn2.setText("三個月到一年左右");
                radioBtn3.setText("一年以上");
                break;
            case 3:
                txtQuestion.setText("3. 請您自我評估使用Anroid手機的熟練度?");
                radioBtn1.setText("還很陌生");
                radioBtn2.setText("一般夠用");
                radioBtn3.setText("非常熟練");
                break;

        }
    }
    

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioBtn1:
                if (checked){
                    Log.d(LOG_TAG,"...radioButton1");
                }
                    // Pirates are the best
                    break;
            case R.id.radioBtn2:
                if (checked){
                    Log.d(LOG_TAG,"...radioButton2");
                }
                    // Ninjas rule
                    break;
            case R.id.radioBtn3:
                if (checked){
                    Log.d(LOG_TAG,"...radioButton3");
                }
                    // Ninjas rule
                    break;
        }
    }
}
