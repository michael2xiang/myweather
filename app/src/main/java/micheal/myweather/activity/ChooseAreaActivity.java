package micheal.myweather.activity;


import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import micheal.myweather.R;
import micheal.myweather.db.WeatherDB;
import micheal.myweather.model.City;
import micheal.myweather.model.County;
import micheal.myweather.model.Province;
import micheal.myweather.util.CustomLog;
import micheal.myweather.util.HttpCallbackListener;
import micheal.myweather.util.HttpUtil;
import micheal.myweather.util.Utility;

/**
 * Created by Administrator on 2014/12/27.
 */
public class ChooseAreaActivity extends Activity {
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;

    private ProgressDialog progressDialog;
    private TextView titleText;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private WeatherDB weatherDB;
    private List<String> dataList = new ArrayList<String>();

    private List<Province> provinceList;
    private List<City> cityList;
    private List<County> countyList;

    private Province selectProvince;
    private City selectCity;

    private int currentLevel;

    private boolean isFromWeatherActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        isFromWeatherActivity = getIntent().getBooleanExtra("from_weather_activity", false);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.getBoolean("city_selected", false) && isFromWeatherActivity == false) {
            Intent intent = new Intent();
            intent.setClass(this, WeatherActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        // super.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choose_area);

        listView = (ListView) findViewById(R.id.list_view);
        titleText = (TextView) findViewById(R.id.title_text);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);
        weatherDB = WeatherDB.getInstance(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel == LEVEL_PROVINCE) {
                    selectProvince = provinceList.get(position);
                    queryCities();
                } else if (currentLevel == LEVEL_CITY) {
                    selectCity = cityList.get(position);
                    queryCounties();
                } else if (currentLevel == LEVEL_COUNTY) {
                    County county = countyList.get(position);
                    CustomLog.vLog("my", county.getCountyName());
                    Intent intent = new Intent(ChooseAreaActivity.this, WeatherActivity.class);
                    intent.putExtra("county_code", county.getCountyCode());
                    startActivity(intent);
                    finish();
                }
            }
        });

        queryProvinces();


    }

    private void queryProvinces() {
        provinceList = weatherDB.loadProvinces();
        if (provinceList.size() > 0) {
            dataList.clear();
            for (Province province : provinceList) {
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText("中国");
            currentLevel = LEVEL_PROVINCE;
        } else {
            queryFromServer(null, "province");
        }
    }


    private void queryCounties() {
        countyList = weatherDB.loadCounties(selectCity.getId());
        if (countyList != null && countyList.size() > 0) {
            dataList.clear();
            for (County county : countyList) {
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText(selectCity.getCityName());
            currentLevel = LEVEL_COUNTY;
        } else {
            queryFromServer(selectCity.getCityCode(), "county");
        }

    }

    private void queryCities() {
        cityList = weatherDB.loadCities(selectProvince.getId());
        if (cityList != null && cityList.size() > 0) {
            dataList.clear();
            for (City city : cityList) {
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText(selectProvince.getProvinceName());
            currentLevel = LEVEL_CITY;
        } else {
            queryFromServer(selectProvince.getProvinceCode(), "city");
        }
    }

    private void queryFromServer(final String code, final String type) {
        String requsetUrl;
        if (code != null && code.length() > 0) {
            requsetUrl = "http://www.weather.com.cn/data/list3/city" + code + ".xml";
        } else {
            requsetUrl = "http://www.weather.com.cn/data/list3/city.xml";
        }
        showProgressDialog();
        HttpUtil.sendHttpRequest(requsetUrl, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                boolean isSuccess = false;
                if ("province".equals(type)) {
                    isSuccess = Utility.hanlderProvincesResponse(weatherDB, response);
                } else if ("city".equals(type)) {
                    isSuccess = Utility.hanlderCitiesResponse(weatherDB, response, selectProvince.getId());
                } else if ("county".equals(type)) {
                    isSuccess = Utility.hanlderCountiesResponse(weatherDB, response, selectCity.getId());
                }
                if (isSuccess) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type)) {
                                queryProvinces();
                            } else if ("city".equals(type)) {
                                queryCities();
                            } else if ("county".equals(type)) {
                                queryCounties();
                            }
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(ChooseAreaActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }

    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("加载中...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (currentLevel == LEVEL_COUNTY) {
            queryCities();
        } else if (currentLevel == LEVEL_CITY) {
            queryProvinces();
        } else if (isFromWeatherActivity) {
            Intent intent = new Intent(this, WeatherActivity.class);
            startActivity(intent);
        } else {
            finish();
        }
    }
}
