package com.yebob.api;

import static android.view.ViewGroup.LayoutParams.FILL_PARENT;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class YebobUI extends Activity {
	public WebView appView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().startSync();
		CookieManager.getInstance().setAcceptCookie(true);
		CookieManager.getInstance().removeExpiredCookie();

        setUpWebView();
    }
    
    @Override
    protected void onResume() {
      super.onResume();
      CookieSyncManager.getInstance().stopSync();
    }

    @Override
    protected void onStop() {
      super.onStop();
      this.appView.destroy();
      this.finish();
    }

    @Override
    protected void onPause() {
      super.onPause();
      CookieSyncManager.getInstance().sync();
    }
        
    private void setUpWebView() {
        this.appView = new WebView(this);
        this.appView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT, 
                1.0F));
        this.appView.setWebViewClient(new YebobUI.YBWebViewClient());
        this.appView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT));
        this.appView.setInitialScale(0);
        this.appView.setVerticalScrollBarEnabled(false);
        this.appView.requestFocusFromTouch();
        
        WebSettings settings = this.appView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setNavDump(true);
        settings.setDatabaseEnabled(true);
        String databasePath = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath(); 
        settings.setDatabasePath(databasePath);
        settings.setDomStorageEnabled(true);
        settings.setGeolocationEnabled(true);

        final RelativeLayout relativeLayout = new RelativeLayout(this);
		final FrameLayout.LayoutParams relativeLayoutLayoutParams = new FrameLayout.LayoutParams(
				FILL_PARENT, FILL_PARENT);
        relativeLayout.addView(this.appView);
        this.setContentView(relativeLayout, relativeLayoutLayoutParams);
    }

	public void visit(String url) {
		this.appView.loadUrl(url);
	}
    
    public void endActivity() {
        this.finish();
    }
    
    private class YBWebViewClient extends WebViewClient {
    	public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }

    	public void onPageStarted(WebView view, String url, Bitmap favicon) {
        }

        public void onPageFinished(WebView view, String url) {
        }

        public void onLoadResource(WebView view, String url) {
        }

        public void onTooManyRedirects(WebView view, Message cancelMsg,
                Message continueMsg) {
            cancelMsg.sendToTarget();
        }

        public void onReceivedError(WebView view, int errorCode,
                String description, String failingUrl) {
        }

        public void onFormResubmission(WebView view, Message dontResend,
                Message resend) {
            dontResend.sendToTarget();
        }

        public void doUpdateVisitedHistory(WebView view, String url,
                boolean isReload) {
        }
        
        public void onReceivedSslError(WebView view, SslErrorHandler handler,
                SslError error) {
            handler.proceed();
        }

        public void onReceivedHttpAuthRequest(WebView view,
                HttpAuthHandler handler, String host, String realm) {
            handler.cancel();
        }
        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
            return false;
        }
        public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
        }
        public void onScaleChanged(WebView view, float oldScale, float newScale) {
        }
    }
}
