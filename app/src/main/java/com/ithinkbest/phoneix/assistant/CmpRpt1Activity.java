package com.ithinkbest.phoneix.assistant;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class CmpRpt1Activity extends ActionBarActivity {
    static String LOG_TAG = "MARK987";

    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cmp_rpt1);
        setupWebView();

//         webView = (WebView) findViewById(R.id.webView);
//        webView.setHorizontalScrollBarEnabled(true);
//        webView.setVerticalScrollBarEnabled(true);
//        webView.getSettings().setUseWideViewPort(true);
//        //Finally this one worked, show Chinese properly!
//        webView.loadData(getHtml(), "text/html; charset=utf-8", "UTF-8");
    }
    String getHtml(){
        StringBuilder result=new StringBuilder();
        //  result.append("<head><meta name='viewport' content='target-densityDpi=device-dpi'/></head>");
        String s="<head><meta name=viewport content=target-density dpi=medium-dpi, width=device-width/></head>";
        result.append(s);
        result.append("中文是必要的xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy111111234567566576876878798");
//        result.append("<table border='1' cellpadding=\"0\" cellspacing=\"0\">");
//        for (int row=0;row<52;row++){
//            result.append("<tr>");
//            for (int col=0;col<22;col++){
//                result.append("<td>&nbsp;"+col+"yyyyy&nbsp;</td>");
//            }
//            result.append("</tr>");
//
//        }
//        result.append("</table>");
//        result.append("中文是必要的xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy111111234567566576876878798");

        String next[] = {};
        List<String[]> list = new ArrayList<String[]>();

        try {
            CSVReader reader = new CSVReader(new InputStreamReader(getAssets().open("cmpRpt1.csv")));
            while(true) {
                next = reader.readNext();
                if(next != null) {


                    list.add(next);
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        result.append("<table border='1' cellpadding='0' cellspacing='0'>");

        for (int i=0;i<list.size();i++){
            result.append("<tr>");
            for (int j=0;j<list.get(i).length;j++){
              //  Log.d(LOG_TAG,list.get(i)[j]);
                String str=list.get(i)[j].toString().replace(" ","&nbsp;");

                if (j==1){
                    Integer x=Integer.parseInt(str);
                //   str= NumberFormat.getNumberInstance(Locale.US).format(x);
                   // String.format("")
                }
//                String str=list.get(i)[j].toString().replace(" ","_");

                result.append("<td   align='right' nowrap=\"nowrap\">&nbsp;"+str+"&nbsp;</td>");
            }
            result.append("</tr>");
        }
        result.append("</table>");
        return result.toString();

    }
    private void setupWebView() {

        webView = (WebView) findViewById(R.id.webView);
        webView.setHorizontalScrollBarEnabled(true);
        webView.setVerticalScrollBarEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        //Finally this one worked, show Chinese properly!
        webView.loadData(getHtml(), "text/html; charset=utf-8", "UTF-8");
    }

//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.setWebViewClient(new WebViewClient() {
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                webView.loadUrl("javascript:MyApp.resize(document.body.getBoundingClientRect().height)");
//                super.onPageFinished(view, url);
//            }
//        });
//        webView.addJavascriptInterface(this, "MyApp");
//    }
//
//    @JavascriptInterface
//    public void resize(final float height) {
//        CmpRpt1Activity.this.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                webView.setLayoutParams(new LinearLayout.LayoutParams(getResources().getDisplayMetrics().widthPixels, (int) (height * getResources().getDisplayMetrics().density)));
//            }
//        });
//    }
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
