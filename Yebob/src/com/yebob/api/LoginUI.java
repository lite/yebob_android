package com.yebob.api;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class LoginUI extends Dialog {

    private String mUrl;
    private ImageView closeImage;
    private YBWebView webView;
    private FrameLayout layout;

    public LoginUI(Context context, String url) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        mUrl = url;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        layout = new FrameLayout(getContext());

        createCrossImage();

        int crossWidth = closeImage.getDrawable().getIntrinsicWidth();
        setUpWebView(crossWidth / 2);

        layout.addView(closeImage, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        addContentView(layout, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
    }

    private void createCrossImage() {
        closeImage = new ImageView(getContext());
        closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUI.this.dismiss();
            }
        });
        Drawable crossDrawable = getContext().getResources().getDrawable(R.drawable.close);
        closeImage.setImageDrawable(crossDrawable);
        //closeImage.setVisibility(View.INVISIBLE);
    }

    private void setUpWebView(int margin) {
        LinearLayout webViewContainer = new LinearLayout(getContext());
        webView = new YBWebView(getContext());
        webView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT));
        //webView.setVisibility(View.INVISIBLE);
        webView.loadUrl(mUrl);
        webView.setJSHandler(new YBUIHandler() {
            public void onReady(WebView view, String url) {
                String cookie = CookieManager.getInstance().getCookie(url);
                for (String item : cookie.split(";")) {
                    String[] strings = item.split("=");
                    if (strings.length > 1 && strings[0].equals("ue") && !strings[1].equals("")) {
//                        SharedPreferences.Editor editor = settings.edit();
//                        editor.putString("sessionId", strings[1]);
//                        editor.commit();
                    }
                }
            }
        });

        webViewContainer.setPadding(margin, margin, margin, margin);
        webViewContainer.addView(webView);
        layout.addView(webViewContainer);
    }
}