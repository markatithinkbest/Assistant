package com.ithinkbest.phoneix.assistant.survey;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.webkit.WebView;

import com.ithinkbest.phoneix.assistant.R;

import java.util.ArrayList;
import java.util.List;


public class CheckSurveyResult001Activity extends ActionBarActivity {
    static String LOG_TAG = "MARK987 CheckSurveyResult001Activity";
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        setupWebView();

    }


    private void setupWebView() {

        webView = (WebView) findViewById(R.id.webView);
        webView.setHorizontalScrollBarEnabled(true);
        webView.setVerticalScrollBarEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        //Finally this one worked, show Chinese properly!
        webView.loadData(getHtml(), "text/html; charset=utf-8", "UTF-8");
    }

    String getHtml() {
          StringBuilder result = new StringBuilder();
        String s = "<head><meta name=viewport content=target-density dpi=medium-dpi, width=device-width/></head>";
        result.append(s);

        String title = getString(R.string.title_activity_check_survey_result001);


        result.append("<h3>" + title + "</h3>");



        String next[] = {};
        List<String[]> list = new ArrayList<String[]>();



        result.append("<table border='1' cellpadding='0' cellspacing='0'>");


            result.append("<tr><td>AAA</td></tr>");

        result.append("</table>");
        return result.toString();

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_check_survey_result001, menu);
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
}
