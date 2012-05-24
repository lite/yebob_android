package com.yebob.apidemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		addStartGameButton();
		addYebobButton();
		addExitButton();
	}

	private void addStartGameButton() {
		TextView view = (TextView) findViewById(R.id.btnStartGame);
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent game = new Intent(MainActivity.this, GamePlayActivity.class);
				startActivity(game);
			}
		});
	}

	private void addYebobButton() {
		TextView view = (TextView) findViewById(R.id.btnYebob);
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent home = new Intent(MainActivity.this, GameFriendsActivity.class);
				startActivity(home);
			}
		});
	}

	private void addExitButton() {
		TextView view = (TextView) findViewById(R.id.btnExit);
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
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setMessage("Are you sure you want to exit?")
				.setCancelable(false)
				.setTitle("EXIT")
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								MainActivity.this.finish();
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

		AlertDialog alert = builder.create();
		alert.show();
	}
}