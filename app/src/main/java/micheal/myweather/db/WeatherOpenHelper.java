package micheal.myweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2014/12/27.
 */
public class WeatherOpenHelper extends SQLiteOpenHelper {


    public static final String CEATER_PROVINCE = "create table Province (" +
            "id integer primary key autoincrement, " +
            "province_name text, " +
            "province_code text)";
    public static final String CEARTE_CITY = "create table City (" +
            "id integer primary key autoincrement, " +
            "city_name text, "+
            "city_code text, "+
            "province_id integer)";
    public  static  final  String  CREATE_COUNTY ="create table County ( "+
            "id integer primary key autoincrement, " +
            "county_name text, "+
            "county_code text, " +
            "city_id integer)";

    public WeatherOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CEATER_PROVINCE);
        db.execSQL(CEARTE_CITY);
        db.execSQL(CREATE_COUNTY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
