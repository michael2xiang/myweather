package micheal.myweather.util;

/**
 * Created by Administrator on 2014/12/27.
 */
public interface HttpCallbackListener {
    void onFinish(String response);

    void onError(Exception e);
}
