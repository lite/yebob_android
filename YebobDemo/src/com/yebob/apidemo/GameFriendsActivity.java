package com.yebob.apidemo;

import android.os.Bundle;
import com.yebob.api.YebobUI;

public class GameFriendsActivity extends YebobUI
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
//        loadUrl("file:///android_asset/www/index.html");
        loadUrl("http://graph.yebob.com/list/top");
    }
}