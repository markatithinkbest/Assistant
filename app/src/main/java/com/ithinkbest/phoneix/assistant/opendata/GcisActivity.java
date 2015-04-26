package com.ithinkbest.phoneix.assistant.opendata;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import com.ithinkbest.phoneix.assistant.R;
import com.ithinkbest.phoneix.assistant.opendata.CSVReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class GcisActivity extends ActionBarActivity {
    static String LOG_TAG = "MARK987";

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cmp_rpt1);
        setupWebView();

    }

    String getHtml() {
        String assetsFilename="gcis.csv";
        StringBuilder result = new StringBuilder();
         String s = "<head><meta name=viewport content=target-density dpi=medium-dpi, width=device-width/></head>";
        result.append(s);

        String title = "經濟部商業司公開資料供應清單";
        String addr ="http://data.gcis.nat.gov.tw/od/datacategory";
        String date = "2015年04月26日";

        result.append("<h3>" + title + "</h3>");
        result.append("原始檔案製表日期: " + date + "<br>");
        result.append("資料來源: <a href='" + addr + "'> 商工行政資料開放平台</a>" + "<br><br>");


        String next[] = {};
        List<String[]> list = new ArrayList<String[]>();

        try {
            CSVReader reader = new CSVReader(new InputStreamReader(getAssets().open(assetsFilename)));
            while (true) {
                next = reader.readNext();
                if (next != null) {


                    list.add(next);
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        result.append("<table border='1' cellpadding='0' cellspacing='0'>");

        for (int i = 0; i < list.size(); i++) {
            result.append("<tr>");
            for (int j = 0; j < list.get(i).length; j++) {
                //  Log.d(LOG_TAG,list.get(i)[j]);
                String str = list.get(i)[j].toString().replace(" ", "&nbsp;");

//                try {
////                    if (j == 1) {
////                        Integer x = Integer.parseInt(str);
////                        DecimalFormat myFormatter = new DecimalFormat("#,###");
////                        str = myFormatter.format(x);
////                    }
////                    if (j==2){
////                        Double x = Double.parseDouble(str);
////                        DecimalFormat myFormatter = new DecimalFormat("#,###.00");
////                        str = myFormatter.format(x);
////                    }
//
////                    switch (j){
////                        case 1:
////                        case 3:
////                            Integer x = Integer.parseInt(str);
////                            DecimalFormat myFormatter = new DecimalFormat("#,###");
////                            str = myFormatter.format(x);
////                            break;
////                        case 2:
////                            Double y = Double.parseDouble(str);
////                            DecimalFormat myFormatter2 = new DecimalFormat("#,###.00");
////                            str = myFormatter2.format(y);
////                            break;
////
////                    }
//
//                    if (j > 0 && 1 == j % 2) {
//                        Integer x = Integer.parseInt(str);
//                        DecimalFormat myFormatter = new DecimalFormat("#,###");
//                        str = myFormatter.format(x);
//                    }
//                    if (j > 0 && 0 == j % 2) {
//                        Double x = Double.parseDouble(str);
//                        DecimalFormat myFormatter = new DecimalFormat("#,###.00");
//                        str = myFormatter.format(x);
//                    }
//
//                } catch (Exception e) {
//                    // not to
//                }
//                String str=list.get(i)[j].toString().replace(" ","_");
                if (i == 0) { // here get bold effect
                    result.append("<th   align='center' nowrap='nowrap'>&nbsp;" + str + "&nbsp;</th>");
                } else {
                    if (j == 0) {
                        result.append("<th   align='right' nowrap='nowrap'>&nbsp;" + str + "&nbsp;</th>");

                    } else {
                        result.append("<td   align='left' nowrap='nowrap'>&nbsp;" + str + "&nbsp;</td>");
                    }
                }
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
