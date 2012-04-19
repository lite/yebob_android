package com.yebob.api;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Api {
	private final static String LOG_TAG = "YebobApi";
	private final static String URL_PREFIX = "http://api.yebob.com";
	
	public static void accessToken(String app_id, String secret, Handler handler) {
		String url = String.format("%s/access_token?app_id=%s&secret=%s", URL_PREFIX, app_id, secret);

		get(new HttpGet(url), handler);
	}
	
	public static void login(String token, String community, String code, String state, String redirect_url, Handler handler) {
		String url = String.format("%s/login?community=%s&code=%s&state=%s&redirect_url=%s", 
				URL_PREFIX, community, code, state, redirect_url);

		getWithToken(token, url, handler);
	}
	
	public static void logout(String token, String session, Handler handler) {
		String url = String.format("%s/logout", URL_PREFIX);

		getWithTokenSession(token, session, url, handler);
	}
	
	public static void me(String token, String session, Handler handler) {
		String url = String.format("%s/me", URL_PREFIX);

		getWithTokenSession(token, session, url, handler);
	}
	
	public static void share(String token, String session, String text, Handler handler) {
		String url = String.format("%s/share?text=%s", URL_PREFIX, text);

		getWithTokenSession(token, session, url, handler);
	}
	
	public static void scoreSubmit(String token, String session, String list_id, long score, Handler handler) {
		String url = String.format("%s/score/submit?list_id=%s&score=%d", 
				URL_PREFIX, list_id, score);

		getWithTokenSession(token, session, url, handler);
	}
	
	public static void rankingLists(String token, Handler handler) {
		String url = String.format("%s/ranking/lists", URL_PREFIX);

		getWithToken(token, url, handler);
	}
	
	public static void rankingTops(String token, String list_id, int count, int start_row, String time_type, String relation_type, Handler handler) {
		String url = String.format("%s/ranking/tops?list_id=%s&count=%d&start_row=%d&time_type=%s&relation_type=%s", 
				URL_PREFIX, list_id, count, start_row, time_type, relation_type);

		getWithToken(token, url, handler);
	}
	
   	public static void statusGet(String token, Handler handler) {
		String url = String.format("%s/status/get",URL_PREFIX);
		
		getWithToken(token, url, handler);
	}
   	
	public static void statusExists(String token, Handler handler) {
		String url = String.format("%s/status/exists",URL_PREFIX);
		
		getWithToken(token, url, handler);
	}

	// ##########
	private static void getWithTokenSession(String token, String session, String url, Handler handler) {
		HttpGet request = new HttpGet(url);
		request.addHeader("access_token", token);
		request.addHeader("session", session);
		get(request, handler);
	}

	private static void getWithToken(String token, String url, Handler hanlder) {
		HttpGet request = new HttpGet(url);
		request.addHeader("access_token", token);
		get(request, hanlder);
	}
	
	private static void get(HttpGet request, Handler handler) {
		if(handler == null){
			handler = new Handler(){
				@Override
				public void execute(JSONObject json) throws JSONException {
					defaultHandler(json);
				}
				
				@Override
				public void onError(int code, String msg){
					defaultErrorHandler(code, msg);
				}
			};
		}
		try {
			BasicHttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, 3000);
			HttpConnectionParams.setSoTimeout(httpParameters, 5000);
			HttpClient client = new DefaultHttpClient(httpParameters);
		    
			Log.d(LOG_TAG, "send " + System.currentTimeMillis());
			HttpResponse response = client.execute(request);
		    Log.d(LOG_TAG, "recv " + System.currentTimeMillis());
			HttpEntity entity = response.getEntity(); 
			if (entity != null) {  
		        String result = EntityUtils.toString(entity);
		        handleResponse(result, handler);
		    }
		} catch (IOException e) {
			Log.e(LOG_TAG, e.toString());
			handler.onError(Error.HTTP_HAS_EXCEPTION, e.toString());
		}
	}
	
	private static void handleResponse(String response, Handler handler ) {
		if (response == null){
			String msg = "sorry, I can't get the data from server.";
			handler.onError(Error.RESPONSE_IS_NONE, msg);
			return;
		}
		try {
			JSONObject json = new JSONObject(response);
			if(json.has("ret")){
				String msg = json.getString("msg");
				handler.onError(Error.RESPONSE_HAS_ERROR, msg);
			}else{
				if (handler != null){
					handler.execute(json);
				}
			}
		} catch (Exception e) {
			handler.onError(Error.RESPONSE_HAS_EXCEPTION, response);
		}
	}
	
	private static void defaultHandler(JSONObject json) throws JSONException 
    {
        Log.e(LOG_TAG, json.toString());
    }
	
	private static void defaultErrorHandler(int code, String msg)
    {
        Log.e(LOG_TAG, "code:" + code + ", msg: {1}" + msg);
    }
}
