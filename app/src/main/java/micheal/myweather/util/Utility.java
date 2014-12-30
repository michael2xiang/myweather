package micheal.myweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import micheal.myweather.db.WeatherDB;
import micheal.myweather.model.City;
import micheal.myweather.model.County;
import micheal.myweather.model.Province;

/**
 * Created by Administrator on 2014/12/27.
 */
public class Utility {
    public synchronized static  boolean hanlderProvincesResponse(WeatherDB weatherDB,String response)
    {
        if(response!=null && response.length()>0)
        {
            String[] allProvinces = response.split(",");
            if(allProvinces != null && allProvinces.length>0){
                for(String item : allProvinces)
                {
                    String[] array = item.split("\\|");
                    Province province  = new Province();
                    province.setProvinceCode(array[0]);
                    province.setProvinceName(array[1]);
                    weatherDB.saveProvince(province);
                }
                return  true;
            }
        }
        return  false;
    }

    public synchronized static  boolean hanlderCitiesResponse(WeatherDB weatherDB,String response,int provinceId)
    {
        if(response!=null && response.length()>0)
        {
            String[] allCities = response.split(",");
            if(allCities != null && allCities.length>0){
                for(String item : allCities)
                {
                    String[] array = item.split("\\|");
                    City city   = new City();
                    city.setCityCode(array[0]);
                    city.setCityName(array[1]);
                    city.setProvinceId(provinceId);
                    weatherDB.saveCity(city);
                }
                return  true;
            }
        }
        return  false;
    }

    public synchronized static  boolean hanlderCountiesResponse(WeatherDB weatherDB,String response,int cityId)
    {
        if(response!=null && response.length()>0)
        {
            String[] allCounties = response.split(",");
            if(allCounties != null && allCounties.length>0){
                for(String item : allCounties)
                {
                    String[] array = item.split("\\|");
                    County county   = new County();
                    county.setCountyCode(array[0]);
                    county.setCountyName(array[1]);
                    county.setCityId(cityId);
                    weatherDB.saveCounty(county);
                }
                return  true;
            }
        }
        return  false;
    }

    public static void handlerWeatherResponse(Context context,String response)
    {
        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
            String cityName = weatherInfo.getString("city");
            String weatherCode = weatherInfo.getString("cityid");
            String temp1 = weatherInfo.getString("temp1");
            String temp2 = weatherInfo.getString("temp2");
            String weatherDesp = weatherInfo.getString("weather");
            String publishTime =weatherInfo.getString("ptime");
            saveWeatherInfo(context,cityName,weatherCode,temp1,temp2,weatherDesp,publishTime);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private static void saveWeatherInfo(Context context, String cityName, String weatherCode, String temp1, String temp2, String weatherDesp, String publishTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("city_selected", true);
        editor.putString("city_name",cityName);
        editor.putString("weather_code",weatherCode);
        editor.putString("temp1",temp1);
        editor.putString("temp2",temp2);
        editor.putString("weather_desp",weatherDesp);
        editor.putString("publish_time",publishTime);
        editor.putString("current_date",sdf.format(new Date()));
        editor.commit();
    }


}
