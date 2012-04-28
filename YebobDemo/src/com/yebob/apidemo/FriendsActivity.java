package com.yebob.apidemo;

import com.yebob.api.YebobUI;

import android.os.Bundle;

public class FriendsActivity extends YebobUI
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
//        visit("file:///android_asset/www/index.html");
        visit("http://alpha.yebob.com/mobui_demo/");
    }
}