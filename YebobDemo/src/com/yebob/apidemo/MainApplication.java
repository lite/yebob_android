package com.yebob.apidemo;

import android.app.Application;
import com.yebob.api.YebobApi;

public class MainApplication extends Application {

    // http://developer.yebob.com/platform/#ManageAppKeysPlace:default
    // private static final String APP_KEY = "30wvt3bn-ohrh-2t5q-wxys-rjberbmvot1c";
    // private static final String APP_SECRET = "cv2922qf-k8ky-x28f-tx2e-c285cajck45c";

    // http://developer.yebob.com/platform/#ManageListsPlace:default
    // private static final String APP_LISTID = "top10";
    // private static final String APP_LISTID = "h2l9n8ryk3";

    @Override
    public void onCreate() {
        super.onCreate();

        String app_key = getResources().getString(R.string.app_key);
        String app_secret = getResources().getString(R.string.app_secret);
        YebobApi.getInstance().init(getApplicationContext(), app_key, app_secret);

//        YebobApi.getInstance().init(getApplicationContext(), APP_KEY, APP_SECRET);
    }
}
