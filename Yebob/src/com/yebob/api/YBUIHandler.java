package com.yebob.api;

import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;

public class YBUIHandler {
    public void onReady(String url) {
    }

    public void onLogin(String sessionId)
    {
    }

    public String getInstallUrl() {
        return "javascript:done()";
    }
}
