package com.ithinkbest.phoneix.assistant;

import android.app.Activity;
import android.app.ListActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class ListViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        String[] itemData = new String[]{

                "農、林、漁、牧業",
                "礦業及土石採取業",
                "製造業",
                "食品製造業",
                "飲料製造業",
                "菸草製造業",
                "紡織業",
                "成衣及服飾品製造業",
                "皮革、毛皮及其製品製造業",
                "木竹製品製造業",
                "紙漿、紙及紙製品製造業",
                "印刷及資料儲存媒體複製業",
                "石油及煤製品製造業",
                "化學材料製造業",
                "化學製品製造業",
                "藥品及醫用化學製品製造業",
                "橡膠製品製造業",
                "塑膠製品製造業",
                "非金屬礦物製品製造業",
                "基本金屬製造業",
                "金屬製品製造業",
                "電子零組件製造業",
                "電腦、電子產品及光學製品製造業",
                "電力設備製造業",
                "機械設備製造業",
                "汽車及其零件製造業",
                "其他運輸工具及其零件製造業",
                "家具製造業",
                "其他製造業",
                "產業用機械設備維修及安裝業",
                "電力及燃氣供應業",
                "用水供應及污染整治業",
                "營造業",
                "批發及零售業",
                "運輸及倉儲業",
                "住宿及餐飲業",
                "資訊及通訊傳播業",
                "金融及保險業",
                "不動產業",
                "專業、科學及技術服務業",
                "支援服務業",
                "公共行政及國防；強制性社會安全",
                "教育服務業",
                "醫療保健及社會工作服務業",
                "藝術、娛樂及休閒服務業",
                "其他服務業",
                "未分類"
        };


        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                itemData));
//
//        listView.setTextFilterEnabled(true);

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                // When clicked, show a toast with the TextView text
//                Toast.makeText(getApplicationContext(),
//                        ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
//            }
//        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_view, menu);
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
