package com.ithinkbest.phoenix001.survey;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.ithinkbest.phoenix001.R;


public class SurveyActivity extends ActionBarActivity {
    static String LOG_TAG = "MARK987 SurveyActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_survey, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    public void onClickBtn1(View view) {
//        Log.d(LOG_TAG, "...btn1");
        Intent intent = new Intent(this, Survey001Activity.class);
        intent.putExtra(Common.EXTRA_MESSAGE, Common.MODE_SURVEY);
        startActivity(intent);



    }
    public void onClickBtn2(View view) {
//        Log.d(LOG_TAG,"...btn2");
        Intent intent = new Intent(this, Survey002Activity.class);
        startActivity(intent);


    }
    public void onClickBtn3(View view) {
//        Log.d(LOG_TAG,"...btn3");

    }
}
