package com.yebob.apidemo;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class GameOptionsActivity extends PreferenceActivity {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.options);
	}

}
