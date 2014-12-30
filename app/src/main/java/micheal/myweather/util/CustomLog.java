package micheal.myweather.util;
import android.util.Log;

/**
 * Created by Administrator on 2014/12/29.
 */
public  class CustomLog {

    public static final  int VERBOSE = 1;
    public static final int DEBUG = 2;
    public static final int INFO = 3;
    public static final int WARN = 4;
    public static final int ERROR = 5;
    public static final int SHOWALL = 6;

    public  static final int LEVELFORSHOW = SHOWALL;
    public static void vLog(String tag,String msg)
    {
        if(VERBOSE<=LEVELFORSHOW)
        {
            Log.v(tag,msg);
        }
    }

    public static void dLog(String tag,String msg)
    {
        if(VERBOSE<=LEVELFORSHOW)
        {
            Log.d(tag,msg);
        }
    }

    public static void iLog(String tag,String msg)
    {
        if(VERBOSE<=LEVELFORSHOW)
        {
            Log.i(tag,msg);
        }
    }
    public static void wLog(String tag,String msg)
    {
        if(VERBOSE<=LEVELFORSHOW)
        {
            Log.w(tag,msg);
        }
    }
    public static void eLog(String tag,String msg)
    {
        if(VERBOSE<=LEVELFORSHOW)
        {
            Log.e(tag,msg);
        }
    }

}
