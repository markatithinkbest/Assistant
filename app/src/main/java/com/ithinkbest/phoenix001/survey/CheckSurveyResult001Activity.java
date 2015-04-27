package com.ithinkbest.phoenix001.survey;

import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.webkit.WebView;

import com.ithinkbest.phoenix001.R;
import com.ithinkbest.phoenix001.SurveyProvider;

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
        showResult();
//        webView.loadData(getHtml(), "text/html; charset=utf-8", "UTF-8");
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

    public void showResult() {
        new AsyncTask<Void, Void, String>() {

            private Cursor getCnt() {
                Uri uri = SurveyProvider.CONTENT_URI_RAW_QUERY;
//                String[] projection = new String[]{SurveyProvider.COLUMN_ID,
//                        SurveyProvider.COLUMN_ANS01, SurveyProvider.COLUMN_ANS02,SurveyProvider.COLUMN_ANS03};
                //
                String selection = SurveyProvider.COLUMN_QUESTION_ID + "=\"" + Survey001Activity.QUESTION_ID + "\"";

                return managedQuery(uri, null, selection, null, null);


                //return null;
            }

            @Override
            protected String doInBackground(Void... params) {
                StringBuilder html = null;
                try {
                    Cursor cursor;
                    String[] projection = new String[]{
                            SurveyProvider.COLUMN_QUESTION_ID,
                            SurveyProvider.COLUMN_ANS01,
                            SurveyProvider.COLUMN_ANS02,
                            SurveyProvider.COLUMN_ANS03,


                    };
                    String selection = SurveyProvider.COLUMN_QUESTION_ID + "='" + Survey001Activity.QUESTION_ID + "'";
                    cursor = getContentResolver().query(SurveyProvider.CONTENT_URI, projection, selection, null, null);


                    if (cursor.moveToFirst()) {
                        do {
                            Log.d(LOG_TAG, " ...@@@ " + cursor.getString(0) + "," + cursor.getString(1) + "," + cursor.getString(2) + "," + cursor.getString(3));
                        } while (cursor.moveToNext());
                    }
                    cursor.close();
                } catch (Exception e) {
                    Log.d(LOG_TAG, " ###showResult " + e.toString());
                    e.printStackTrace();
                }


                html = new StringBuilder();
                String s = "<head><meta name=viewport content=target-density dpi=medium-dpi, width=device-width/></head>";
                html.append(s);

                String title = getString(R.string.title_activity_check_survey_result001);


                html.append("<h3>" + title + "</h3>");
//                html.append("" + title + "");
                html.append("<table>");
                for (int i = 0; i < Survey001Activity.QUESTION_SET.length; i++) {
//                    html.append("<tr><td  nowrap='nowrap'>");
                    html.append("<tr><td  >");

//                    html.append("<h3>").append(Survey001Activity.QUESTION_SET[i][0]).append("</h3>");
                    html.append("").append(Survey001Activity.QUESTION_SET[i][0]).append("");
                    html.append("</td></tr>");
                    html.append("<tr><td  nowrap='nowrap'>");
                    html.append("<table border='1' cellpadding='0' cellspacing='0'>");
                    html.append("<tr>")
                            .append("<th  align='center' nowrap='nowrap'>&nbsp;")
                            .append(" ")
                            .append("&nbsp;</th>")
                            .append("<th  align='center' nowrap='nowrap'>&nbsp;")
                            .append("個數 ")
                            .append("&nbsp;</th>")
                            .append("<th  align='center' nowrap='nowrap'>&nbsp;")
                            .append("比例 ")
                            .append("&nbsp;</th>")
                            .append("</tr>");

                    for (int j = 1; j <= 3; j++) {
                        html.append("<tr>")
                                .append("<th  align='center' nowrap='nowrap'>&nbsp;")
                                .append(Survey001Activity.QUESTION_SET[i][j])
                                .append("&nbsp;</th>")
                                .append("<td  align='center' nowrap='nowrap'>&nbsp;")
                                .append("111 ")
                                .append("&nbsp;</td>")
                                .append("<td  align='center' nowrap='nowrap'>&nbsp;")
                                .append("33% ")
                                .append("&nbsp;</td>")
                                .append("</tr>");
//                        html.append("<tr>").append("<th>&nbsp;").append(Survey001Activity.QUESTION_SET[i][2]).append("&nbsp;</th>").append("</tr>");
//                        html.append("<tr>").append("<th>&nbsp;").append(Survey001Activity.QUESTION_SET[i][3]).append("&nbsp;</th>").append("</tr>");
                    }
                    html.append("</table>");
                    html.append("</table><br>");

                    html.append("</td></tr>");
                }
                html.append("<table>");

                //  Log.d(LOG_TAG, "...doInBackground ...GOING TO DO CONTENT PROVIDER");
                return html.toString();
            }

            @Override
            protected void onPostExecute(String msg) {
                webView.loadData(msg, "text/html; charset=utf-8", "UTF-8");
            }
        }.execute(null, null, null);

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
