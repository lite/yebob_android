package com.yebob.apidemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MenuActivity extends Activity {

	private Typeface font;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		font = Typeface.createFromAsset(getAssets(), "font/pf_tempesta_five.ttf");
		
		addAppName();
		addStartGameButton();
		addFriendsButton();
		addDemoButton();
		addOptionButton();
		addExitButton();
	}

	private void addAppName() {
		TextView view = (TextView) findViewById(R.id.textAppName);
		view.setTypeface(this.font);
		view.setTextColor(Color.BLACK);
	}
	
	private void addStartGameButton() {
		TextView view = (TextView) findViewById(R.id.btnStartGame);
		view.setTypeface(this.font);
		view.setTextColor(Color.BLACK);
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent game = new Intent(MenuActivity.this, GameExampleActivity.class);
				startActivity(game);
			}
		});
	}

	private void addFriendsButton() {
		TextView view = (TextView) findViewById(R.id.btnFriends);
		view.setTypeface(this.font);
		view.setTextColor(Color.BLACK);
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent friends = new Intent(MenuActivity.this, FriendsActivity.class);
				startActivity(friends);
			}
		});
	}

	private void addDemoButton() {
		TextView view = (TextView) findViewById(R.id.btnDemo);
		view.setTypeface(this.font);
		view.setTextColor(Color.BLACK);
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent demo = new Intent(MenuActivity.this, YebobApiActivity.class);
				startActivity(demo);
			}
		});
	}

	private void addOptionButton() {
		TextView view = (TextView) findViewById(R.id.btnOptions);
		view.setTypeface(this.font);
		view.setTextColor(Color.BLACK);
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent options = new Intent(MenuActivity.this, OptionsActivity.class);
				startActivity(options);
			}
		});
	}

	private void addExitButton() {
		TextView view = (TextView) findViewById(R.id.btnExit);
		view.setTypeface(this.font);
		view.setTextColor(Color.BLACK);
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showExitDialog();
			}
		});
	}

	@Override
	public boolean onKeyDown(final int pKeyCode, final KeyEvent pEvent) {

		if (pKeyCode == KeyEvent.KEYCODE_BACK
				&& pEvent.getAction() == KeyEvent.ACTION_DOWN) {
			showExitDialog();
			return true;
		}

		return super.onKeyDown(pKeyCode, pEvent);
	}

	public void showExitDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);
		builder.setMessage("Are you sure you want to exit?")
				.setCancelable(false)
				.setTitle("EXIT")
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								MenuActivity.this.finish();
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

		AlertDialog alert = builder.create();
		alert.setIcon(R.drawable.icon);
		alert.show();
	}
}