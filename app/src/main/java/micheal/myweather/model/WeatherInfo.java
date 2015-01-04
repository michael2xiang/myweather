package micheal.myweather.model;

import com.google.gson.annotations.Expose;

/**
 * Created by Administrator on 2015/1/4.
 */
public class WeatherInfo {
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityid() {
        return cityid;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
    }

    public String getTemp1() {
        return temp1;
    }

    public void setTemp1(String temp1) {
        this.temp1 = temp1;
    }

    public String getTemp2() {
        return temp2;
    }

    public void setTemp2(String temp2) {
        this.temp2 = temp2;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getPtime() {
        return ptime;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
    }

    @Expose
    private String city;
    @Expose
    private  String cityid;
    @Expose
    private String temp1;
    @Expose
    private String temp2;
    @Expose
    private String weather;
    @Expose
    private String ptime;

}
