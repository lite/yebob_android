package com.yebob.apidemo;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.yebob.api.*;

public class GamePlayActivity extends Activity {
    private static final int DIALOG_LOGIN_ID = 0;

    private long gameScore = 0;
    private EditText editScore;

    public void addScore(long points) {
        gameScore += points;
        editScore.setText(Long.toString(gameScore));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameplay);

        editScore = (EditText) findViewById(R.id.scoreText);

        Button buttonScore1 = ((Button) findViewById(R.id.button_score1));
        buttonScore1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                addScore(1);
            }
        });

        Button buttonScore10 = ((Button) findViewById(R.id.button_score10));
        buttonScore10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                addScore(10);
            }
        });

        Button buttonScore100 = ((Button) findViewById(R.id.button_score100));
        buttonScore100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                addScore(100);
            }
        });

        Button buttonGameOver = ((Button) findViewById(R.id.button_game_over));
        buttonGameOver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (YebobApi.getInstance().isSessionValid()){
                    String app_listid = getResources().getString(R.string.app_listid);
                    YebobApi.getInstance().uploadScore(app_listid, gameScore);
                    return;
                }
                showDialog(DIALOG_LOGIN_ID);
            }
        });
    }

    public Dialog onCreateDialog(int id) {
        if (id != DIALOG_LOGIN_ID) return null;

        return new LoginUI(GamePlayActivity.this,
                "http://graph.yebob.com/list/login",
                new YBUIHandler() {
                    public void onReady(String url) {
                        dismissDialog(DIALOG_LOGIN_ID);
                    }
                });
    }
}
