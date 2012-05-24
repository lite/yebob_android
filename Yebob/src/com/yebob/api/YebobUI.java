package com.yebob.api;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.*;

public class YebobUI extends Activity {
    private YBWebView webView;
    private RadioButton buttonHome;
    private RadioButton buttonFriends;
    private RadioButton buttonMarket;
    private ImageView buttonClose;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.yb_yebob);

        title = (TextView) findViewById(R.id.title);

        buttonClose = (ImageView) findViewById(R.id.buttonClose);
        buttonClose.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                YebobUI.this.finish();
            }
        });

        webView = (YBWebView) findViewById(R.id.webview);

        buttonHome = (RadioButton) findViewById(R.id.yb_shortcut_home);
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title.setText(getString(R.string.home_title));
                loadUrl(getString(R.string.home_url));
            }
        });

        buttonFriends = (RadioButton) findViewById(R.id.yb_shortcut_friends);
        buttonFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title.setText(getString(R.string.featuregames_title));
                loadUrl(getString(R.string.featuregames_url));
            }
        });

        buttonMarket = (RadioButton) findViewById(R.id.yb_shortcut_market);
        buttonMarket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title.setText(getString(R.string.friend_title));
                loadUrl(getString(R.string.friend_url));
            }
        });
    }

    public void loadUrl(String url) {
        webView.setJSHandler(new YBUIHandler());
        webView.loadUrl(url);
    }
}
