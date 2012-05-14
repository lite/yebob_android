package com.yebob.api;

import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;

public class YBUIHandler {
    public void onReady(WebView view, String url) {
    }

    public String getInstallUrl() {
        return "javascript:done()";
    }
}
