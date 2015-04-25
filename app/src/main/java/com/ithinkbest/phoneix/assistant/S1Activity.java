package com.ithinkbest.phoneix.assistant;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;


public class S1Activity extends ActionBarActivity {
    static String LOG_TAG = "MARK987";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s1);
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

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioButton1:
                if (checked){
                    Log.d(LOG_TAG,"...radioButton1");
                }
                    // Pirates are the best
                    break;
            case R.id.radioButton2:
                if (checked){
                    Log.d(LOG_TAG,"...radioButton2");
                }
                    // Ninjas rule
                    break;
            case R.id.radioButton3:
                if (checked){
                    Log.d(LOG_TAG,"...radioButton3");
                }
                    // Ninjas rule
                    break;
        }
    }
}
