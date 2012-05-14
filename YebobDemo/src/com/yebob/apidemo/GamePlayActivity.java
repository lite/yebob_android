package com.yebob.apidemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.yebob.api.*;
import org.json.JSONException;
import org.json.JSONObject;

public class GamePlayActivity extends Activity {

    private static final int DIALOG_LOGIN_ID = 0;

    private long gameScore = 0;
    private EditText editScore;

    public void addScore(long points) {
        editScore.setText("" + (gameScore + points));
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
                uploadScore(gameScore);
            }
        });
    }

    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        ;
        switch (id) {
            case DIALOG_LOGIN_ID:
                dialog = new LoginUI(this, "http://www.douban.com/");
                break;
            default:
                break;
        }
        return dialog;
    }

    private void uploadScore(long scoreResult) {
        SharedPreferences settings = getSharedPreferences("YBPrefsFile", 0);
        String sessionId = settings.getString("sessionId", "");
        if (sessionId.length() > 0) {
            // if session is valid, then do
            if (isSessionValid(sessionId)) {
                String token = "";
                String listId = "";
                Api.scoreSubmit(token, sessionId, listId, scoreResult, new YBHandler() {
                    @Override
                    public void execute(JSONObject json) throws JSONException {
                        showMessage("upload successully.");
                    }
                });
                return;
            }
        }

        showDialog(DIALOG_LOGIN_ID);
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    private boolean isSessionValid(String sessionId) {
        return false;  //To change body of created methods use File | Settings | File Templates.
    }
}
