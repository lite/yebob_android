package com.yebob.api;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.http.SslError;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.webkit.*;

public class YBWebView extends WebView {
    public static final String INTERFACE_NAME = "yebob";
    private static final int SWIPE_MIN_DISTANCE = 320;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    private ProgressDialog progressBar;
    private YBUIHandler jsHandler = null;
    private GestureDetector gd = null;
    private boolean flinged;

    public YBWebView(Context context) {
        super(context);
        init(context);
    }

    public YBWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void setJSHandler(YBUIHandler handler){
        jsHandler = handler;
        addJavascriptInterface(new JSInterface(), INTERFACE_NAME);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gd.onTouchEvent(event);
        if (flinged) {
            flinged = false;
            return true;
        } else {
            return super.onTouchEvent(event);
        }
    }

    GestureDetector.SimpleOnGestureListener sogl = new GestureDetector.SimpleOnGestureListener() {
        // your fling code here
        public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
            if (event1.getX() < 1200 && event1.getX() > 80) {
                return false;
            }
            if (Math.abs(event1.getY() - event1.getY()) > SWIPE_MAX_OFF_PATH)
                return false;
            if(event1.getX() - event2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                loadUrl("javascript:changePage('LEFT')");
                Log.i("Swiped", "swipe left");
                flinged = true;
            } else if (event2.getX() - event1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                loadUrl("javascript:changePage('RIGHT')");
                Log.i("Swiped","swipe right");
                flinged = true;
            }
            return true;
        }
    };

    private void init(Context context) {
        gd = new GestureDetector(context, sogl);

        CookieSyncManager.createInstance(context);
        CookieManager.getInstance().setAcceptCookie(true);

        WebSettings settings = this.getSettings();
        settings.setJavaScriptEnabled(true);

        setWebViewClient(new YBWebViewClient());
    }

    private class JSInterface {
        public void onInstall() {
            Handler handler = new Handler();
            handler.post(new Runnable() {
                public void run() {
                    loadUrl(jsHandler.getInstallUrl());
                }
            });
        }
    }

    private void updateSession(String sessionId) {
        SharedPreferences settings = getContext().getSharedPreferences(YebobApi.YB_PREFS, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("sessionId", sessionId);
        editor.commit();
    }

    private class YBWebViewClient extends WebViewClient {
        public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
            String title = getResources().getString(R.string.title_loading);
            String message = getResources().getString(R.string.message_loading);
            if(progressBar == null) progressBar = ProgressDialog.show(getContext(), title, message);
            else progressBar.show();
            String token =  YebobApi.getInstance().getToken();
            CookieManager.getInstance().setCookie(url, "access_token=" + token);
        }

        public void onPageFinished(WebView view, String url){
            if (progressBar != null && progressBar.isShowing()) {
                progressBar.hide();
            }

            String cookie = CookieManager.getInstance().getCookie(url);
            if(cookie!=null){
                for (String item : cookie.split(";")) {
                    String[] strings = item.split("=");
                    if (strings.length > 1 ){
                        if(strings[0].trim().equals("session")){
                            updateSession(strings[1].trim());
                            jsHandler.onReady(url);
                        }
                    }
                }
            }
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }

        public void onTooManyRedirects(WebView view, Message cancelMsg,
                Message continueMsg) {
            cancelMsg.sendToTarget();
        }

        public void onFormResubmission(WebView view, Message dontResend,
                Message resend) {
            dontResend.sendToTarget();
        }

        public void onReceivedSslError(WebView view, SslErrorHandler handler,
                SslError error) {
            handler.proceed();
        }

        public void onReceivedHttpAuthRequest(WebView view,
                HttpAuthHandler handler, String host, String realm) {
            handler.cancel();
        }
    }
}
