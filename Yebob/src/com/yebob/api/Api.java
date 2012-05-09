package com.yebob.api;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

public class Api {
	private final static String LOG_TAG = "YebobApi";
	private final static String URL_PREFIX = "http://api.yebob.com";
	
	public static void accessToken(String app_id, String secret, YBHandler YBHandler) {
		String url = String.format("%s/access_token?app_id=%s&secret=%s", URL_PREFIX, app_id, secret);

		get(new HttpGet(url), YBHandler);
	}
	
	public static void login(String token, String community, String code, String state, String redirect_url, YBHandler YBHandler) {
		String url = String.format("%s/login?community=%s&code=%s&state=%s&redirect_url=%s", 
				URL_PREFIX, community, code, state, redirect_url);

		getWithToken(token, url, YBHandler);
	}
	
	public static void logout(String token, String session, YBHandler YBHandler) {
		String url = String.format("%s/logout", URL_PREFIX);

		getWithTokenSession(token, session, url, YBHandler);
	}
	
	public static void me(String token, String session, YBHandler YBHandler) {
		String url = String.format("%s/me", URL_PREFIX);

		getWithTokenSession(token, session, url, YBHandler);
	}
	
	public static void share(String token, String session, String text, YBHandler YBHandler) {
		String url = String.format("%s/share?text=%s", URL_PREFIX, text);

		getWithTokenSession(token, session, url, YBHandler);
	}
	
	public static void scoreSubmit(String token, String session, String list_id, long score, YBHandler YBHandler) {
		String url = String.format("%s/score/submit?list_id=%s&score=%d", 
				URL_PREFIX, list_id, score);

		getWithTokenSession(token, session, url, YBHandler);
	}
	
	public static void rankingLists(String token, YBHandler YBHandler) {
		String url = String.format("%s/ranking/lists", URL_PREFIX);

		getWithToken(token, url, YBHandler);
	}
	
	public static void rankingTops(String token, String list_id, int count, int start_row, String time_type, String relation_type, YBHandler YBHandler) {
		String url = String.format("%s/ranking/tops?list_id=%s&count=%d&start_row=%d&time_type=%s&relation_type=%s", 
				URL_PREFIX, list_id, count, start_row, time_type, relation_type);

		getWithToken(token, url, YBHandler);
	}
	
   	public static void statusGet(String token, YBHandler YBHandler) {
		String url = String.format("%s/status/get",URL_PREFIX);
		
		getWithToken(token, url, YBHandler);
	}
   	
	public static void statusExists(String token, YBHandler YBHandler) {
		String url = String.format("%s/status/exists",URL_PREFIX);
		
		getWithToken(token, url, YBHandler);
	}

    // app 
 	public static void showDetail(Application my, String appId){
 		Uri uri = Uri.parse("market://details?id=" + appId);  
 		Intent intent = new Intent(Intent.ACTION_VIEW, uri); 
 		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
 		my.startActivity(intent); 
 	}
 	
 	private void installApk(Application my, String pathApk){
 		Intent intent = new Intent(Intent.ACTION_VIEW);
 		intent.setDataAndType(Uri.fromFile(new File(pathApk)), 
 				"application/vnd.android.package-archive");
 		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
 		my.startActivity(intent);  
 	}
 	
 	private void downloadApk(String urlApk, String apkName){
         try{
             URL url = new URL(urlApk);
             HttpURLConnection c = (HttpURLConnection) url.openConnection();
             c.setRequestMethod("GET");
             c.setDoOutput(true);
             c.connect(); 

             String PATH = Environment.getExternalStorageDirectory() + "/download/";
             File file = new File(PATH); 
             if (!file.exists()) {
                 file.mkdirs();
             }
             File outputFile = new File(file, apkName);           
             FileOutputStream fos = new FileOutputStream(outputFile);
             InputStream is = c.getInputStream();

             byte[] buffer = new byte[1024];
             int len1 = 0;
             while ((len1 = is.read(buffer)) != -1) {
                 fos.write(buffer, 0, len1); 
             }
             fos.close();
             is.close();
         } 
         catch (IOException e) 
         {
         	defaultErrorHandler(Error.DOWNLOAD_ERROR, e.toString());
         }   
 	}
 	
	public static void defaultErrorHandler(int code, String msg)
    {
        Log.e(LOG_TAG, "code:" + code + ", msg: {1}" + msg);
    }
	
	// ##########
	private static void getWithTokenSession(String token, String session, String url, YBHandler YBHandler) {
		HttpGet request = new HttpGet(url);
		request.addHeader("access_token", token);
		request.addHeader("session", session);
		get(request, YBHandler);
	}

	private static void getWithToken(String token, String url, YBHandler hanlder) {
		HttpGet request = new HttpGet(url);
		request.addHeader("access_token", token);
		get(request, hanlder);
	}
	
	private static void get(HttpGet request, YBHandler YBHandler) {
		if(YBHandler == null){
			YBHandler = new YBHandler(){
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
		        handleResponse(result, YBHandler);
		    }
		} catch (IOException e) {
			Log.e(LOG_TAG, e.toString());
			YBHandler.onError(Error.HTTP_HAS_EXCEPTION, e.toString());
		}
	}
	
	private static void handleResponse(String response, YBHandler YBHandler) {
		if (response == null){
			String msg = "sorry, I can't get the data from server.";
			YBHandler.onError(Error.RESPONSE_IS_NONE, msg);
			return;
		}
		try {
			JSONObject json = new JSONObject(response);
			if(json.has("ret")){
				String msg = json.getString("msg");
				YBHandler.onError(Error.RESPONSE_HAS_ERROR, msg);
			}else{
				if (YBHandler != null){
					YBHandler.execute(json);
				}
			}
		} catch (Exception e) {
			YBHandler.onError(Error.RESPONSE_HAS_EXCEPTION, response);
		}
	}
	
	private static void defaultHandler(JSONObject json) throws JSONException 
    {
        Log.e(LOG_TAG, json.toString());
    }
}
