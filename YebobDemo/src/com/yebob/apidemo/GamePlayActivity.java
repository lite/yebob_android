package com.yebob.apidemo;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.yebob.api.Api;
import com.yebob.api.LoginUI;
import com.yebob.api.YBHandler;
import com.yebob.api.YBUIHandler;
import org.json.JSONException;
import org.json.JSONObject;

public class GamePlayActivity extends Activity {

    private static final int DIALOG_LOGIN_ID = 0;
    public static final String YB_PREFS = "YebobPrefs";

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

    public Dialog onCreateDialog(int id) {
        if (id != DIALOG_LOGIN_ID) return null;

        return new LoginUI(GamePlayActivity.this,
                "http://www.douban.com/",
                new YBUIHandler() {
                    public void onReady(String url) {
                        onLoginUIReady(url);
                    }
                });
    }

    private void onLoginUIReady(String url){
        String cookie = CookieManager.getInstance().getCookie(url);
        for (String item : cookie.split(";")) {
            String[] strings = item.split("=");
            if (strings.length > 1 && strings[0].equals("ue") && !strings[1].equals("")) {
                updateSessionId(strings[1]);
                dismissDialog(DIALOG_LOGIN_ID);
            }
        }
    }

    private void uploadScore(long scoreResult) {
        String token = "";

        SharedPreferences settings = this.getSharedPreferences(YB_PREFS, 0);
        String sessionId = settings.getString("sessionId", "");
        if (isSessionValid(token, sessionId)) {
            String listId = "";
            Api.scoreSubmit(token, sessionId, listId, scoreResult, new YBHandler() {
                @Override
                public void execute(JSONObject json) throws JSONException {
                    showMessage("score uploaded successully.");
                }
            });
            return;
        }
        showDialog(DIALOG_LOGIN_ID);
    }

    private void updateSessionId(String sessionId) {
        SharedPreferences settings = this.getSharedPreferences(YB_PREFS, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("sessionId", sessionId);
        editor.commit();
    }

    private boolean isSessionValid(String token, String sessionId) {
        if (sessionId.length() == 0) return false;
        Api.me(token, sessionId, new YBHandler() {
            @Override
            public void execute(JSONObject json) throws JSONException {
                showMessage(json.getString("community"));
            }
        });
        return true;
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }


}
