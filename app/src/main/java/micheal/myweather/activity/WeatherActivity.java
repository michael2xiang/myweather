package micheal.myweather.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import micheal.myweather.R;
import micheal.myweather.util.CustomLog;
import micheal.myweather.util.HttpCallbackListener;
import micheal.myweather.util.HttpUtil;
import micheal.myweather.util.Utility;

public class WeatherActivity extends Activity implements View.OnClickListener {

    private LinearLayout mWeatherInfoLayout;
    private TextView mCityName;
    private TextView mPublishText;
    private  TextView mWeatherDesp;
    private  TextView mTemp1;
    private TextView mTemp2;
    private TextView mCurrentDate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weather_layout);
        mWeatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
        mCityName = (TextView) findViewById(R.id.city_name);
        mPublishText = (TextView) findViewById(R.id.publish_text);
        mWeatherDesp = (TextView) findViewById(R.id.weather_desp);
        mTemp1 = (TextView) findViewById(R.id.temp1);
        mTemp2 = (TextView) findViewById(R.id.temp2);
        mCurrentDate = (TextView) findViewById(R.id.current_date);

        Button mSwitchCity = (Button) findViewById(R.id.swich_city);
        Button mRefush = (Button) findViewById(R.id.refush_weather);
        mSwitchCity.setOnClickListener(this);
        mRefush.setOnClickListener(this);

        String countyCode = getIntent().getStringExtra("county_code");
        if(countyCode!=null && countyCode.length()>0)
        {
            mPublishText.setText("同步中...");
            mWeatherInfoLayout.setVisibility(View.INVISIBLE);
            mCityName.setVisibility(View.INVISIBLE);
            queryWeatherCode(countyCode);
        }else{
            showWeather();
        }


    }

    private void showWeather() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        mCityName.setText(prefs.getString("city_name",""));
        mTemp1.setText(prefs.getString("temp1",""));
        mTemp2.setText(prefs.getString("temp2",""));
        mWeatherDesp.setText(prefs.getString("weather_desp",""));
        mPublishText.setText(prefs.getString("publish_time","")+"发布");
        mCurrentDate.setText(prefs.getString("current_date",""));
        mWeatherInfoLayout.setVisibility(View.VISIBLE);
        mCityName.setVisibility(View.VISIBLE);

    }

    private void queryWeatherCode(String countyCode) {
       String requsetUrl = "http://www.weather.com.cn/data/list3/city" + countyCode + ".xml";
        queryFromServer(requsetUrl,"countyCode");
    }

    private void queryFromServer(String requsetUrl, final String type) {
        HttpUtil.sendHttpRequest(requsetUrl,new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                if("countyCode".equals(type))
                {
                    CustomLog.vLog("my",response);
                    if(response!=null && response.length()>0)
                    {
                        String[] array = response.split("\\|");
                        if(array!=null && array.length==2)
                        {
                            String weatherCode = array[1];
                            queryWeatherInfo(weatherCode);
                        }
                    }
                } else if("weatherCode".equals(type))
                {
                    CustomLog.vLog("my",response);
                    Utility.handlerWeatherResponse(WeatherActivity.this,response);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showWeather();
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mPublishText.setText("同步失败。");
                    }
                });
            }
        });
    }

    private void queryWeatherInfo(String weatherCode) {
        String requsetUrl = "http://www.weather.com.cn/data/cityinfo/" + weatherCode + ".html";
        queryFromServer(requsetUrl,"weatherCode");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_weather, menu);
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

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.refush_weather:
                mPublishText.setText("同步中...");
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                String weatherCode = preferences.getString("weather_code","");
                if(weatherCode != null && weatherCode.length()>0)
                {
                    queryWeatherInfo(weatherCode);
                }

                break;
            case R.id.swich_city:
                Intent intent = new Intent(this,ChooseAreaActivity.class);
                intent.putExtra("from_weather_activity", true);
                startActivity(intent);
                finish();
                break;

        }
    }
}
