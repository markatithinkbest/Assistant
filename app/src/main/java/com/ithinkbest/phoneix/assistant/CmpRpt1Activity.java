package com.ithinkbest.phoneix.assistant;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;


public class CmpRpt1Activity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cmp_rpt1);

        StringBuilder result=new StringBuilder();
        result.append("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy111111234567566576876878798");
        result.append("<table border='1' cellpadding=\"0\" cellspacing=\"0\"");
        for (int row=0;row<22;row++){
            result.append("<tr>");
            for (int col=0;col<22;col++){
                result.append("<td>"+col+"yyyyy </td>");


            }
            result.append("</tr>");

        }
        result.append("</table>");

        WebView webView = (WebView) findViewById(R.id.webView);
        webView.setHorizontalScrollBarEnabled(true);
        webView.setVerticalScrollBarEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        //Finally this one worked, show Chinese properly!
        webView.loadData(result.toString(), "text/html; charset=utf-8", "UTF-8");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cmp_rpt1, menu);
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
}
