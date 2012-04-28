package com.yebob.apidemo;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.yebob.api.Api;

public class YebobHomeActivity extends TabActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.yebob);

		// rankings
		addTab("Home", R.drawable.home, FriendsActivity.class);
		addTab("Ranking", R.drawable.rankings, FriendsActivity.class);
		addTab("Baton", R.drawable.baton, FriendsActivity.class);
		
		// button
		ImageView btnInstall = (ImageView) findViewById(R.id.btnInstall);
		btnInstall.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Api.showDetail(getApplication(), "com.trackeen.sea");
			}
		});

	}
	
	private void addTab(String label, int resId, Class<?> cls){
		TabHost tabHost = getTabHost();
		TabSpec tabSpec = tabHost.newTabSpec(label);
		tabSpec.setIndicator(label, getResources().getDrawable(resId));
		Intent intent = new Intent(this, cls);
		tabSpec.setContent(intent);
		tabHost.addTab(tabSpec);
	}
}
