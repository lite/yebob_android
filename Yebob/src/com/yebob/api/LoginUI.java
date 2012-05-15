package com.yebob.api;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class LoginUI extends Dialog {
    private YBWebView webView;
    private Context context;
    private YBUIHandler jsHandler;
    private String url;

    public LoginUI(Context context, String url, YBUIHandler jsHandler) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.context = context;
        this.url = url;
        this.jsHandler = jsHandler;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        addContentView(setUpWebView(), new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
    }

    private FrameLayout setUpWebView() {
        FrameLayout layout = new FrameLayout(context);

        ImageView closeImage = new ImageView(context);
        closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUI.this.dismiss();
            }
        });
        Drawable crossDrawable = context.getResources().getDrawable(R.drawable.close);
        closeImage.setImageDrawable(crossDrawable);
        layout.addView(closeImage, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        LinearLayout webViewLayout = new LinearLayout(context);
        webView = new YBWebView(context);
        webView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT));
        webView.setJSHandler(jsHandler);
        webView.loadUrl(url);

        int margin = closeImage.getDrawable().getIntrinsicWidth();
        webViewLayout.setPadding(margin, margin, margin, margin);
        webViewLayout.addView(webView);
        layout.addView(webViewLayout);

        return layout;
    }
}