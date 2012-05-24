package com.yebob.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

public class YebobApi {
    public static final String YB_PREFS = "yb_session_id_test";

    private Context context;
    private Api api;

    public static YebobApi getInstance(Context context, String app_key, String app_secret)
    {
        return new YebobApi(context, app_key, app_secret);
    }

    protected YebobApi(Context context,  String app_key, String app_secret)
    {
        this.context = context;
        api = new Api(app_key, app_secret);
    }

    public void uploadScore(String listId, long score) {
        SharedPreferences settings = context.getSharedPreferences(YB_PREFS, 0);
        String sessionId = settings.getString("sessionId", "");
        if (sessionId.length() == 0) return;

        api.rankingLists(new YBHandler(){
            @Override
            public void execute(JSONObject json) throws JSONException {
                showMessage(json.toString());
            }
        });
        api.scoreSubmit(sessionId, listId, score, new YBHandler(){
            @Override
            public void execute(JSONObject json) throws JSONException {
                showMessage("score has uploaded.");
            }
        });
    }

    public boolean isSessionValid() {
        SharedPreferences settings = context.getSharedPreferences(YB_PREFS, 0);
        String sessionId = settings.getString("sessionId", "");
        if (sessionId.length() == 0) return false;
        api.me(sessionId, new YBHandler() {
            @Override
            public void execute(JSONObject json) throws JSONException {
                showMessage(json.getString("community"));
            }
        });
        return true;
    }

    private void showMessage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
