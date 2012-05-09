package com.yebob.apidemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class GamePlayActivity extends Activity {

	static final int	DIALOG_PROGRESS		= 0;
	static final int	DIALOG_SUBMITTED	= 1;
	static final int	DIALOG_FAILED		= 2;
	
	EditText			scoreField;

	public void addScore(int points) {
		final int old = Integer.parseInt(scoreField.getText().toString());
		scoreField.setText("" + (old + points));
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gameplay);

		scoreField = (EditText) findViewById(R.id.scoreText);

		((Button) findViewById(R.id.button_score1)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				addScore(1);
			}
		});
		((Button) findViewById(R.id.button_score10)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				addScore(10);
			}
		});
		((Button) findViewById(R.id.button_score100)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				addScore(100);
			}
		});

		((Button) findViewById(R.id.button_game_over)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				double scoreResult;
				try {
					scoreResult = Double.valueOf(scoreField.getText().toString());
				} catch(NumberFormatException ex) {
					scoreResult = 0;
				}
				showDialog(DIALOG_PROGRESS);
			}
		});

	}

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_PROGRESS:
			return ProgressDialog
				.show(GamePlayActivity.this, "", getString(R.string.submitting_your_score));
		case DIALOG_SUBMITTED:
			return (new AlertDialog.Builder(this))
				.setMessage(R.string.score_was_submitted)
				.setTitle(R.string.app_name)
				.setPositiveButton(R.string.awesome, null)
				.create();
		case DIALOG_FAILED:
			return (new AlertDialog.Builder(this))
				.setMessage(R.string.score_submit_error)
				.setPositiveButton(R.string.too_bad, null)
				.create();
		}
		return null;
	}
}
