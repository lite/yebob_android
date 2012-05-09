package com.yebob.apidemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.yebob.api.Api;
import com.yebob.api.YBHandler;
import org.json.JSONException;
import org.json.JSONObject;

public class GameYebobApiActivity extends Activity {

	//	http://alpha.yebob.com/platform/#ManageAppKeysPlace:default
	private static final String APP_SECRET = "1dhjid1n-69og-ek25-jngk-7kraqc3tpmgw";
	private static final String APP_ID = "sbqf0xpt-d0c9-rsaz-1ujz-vvgljx9qgfsw";
	
	// sina_weibo
	private static final String SOCIAL_APPCODE = "5a1702c984a84578ada3a56e95c42a5a";
	private static final String SOCIAL_APPID = "sina_weibo";

	private TextView messageView;

	// for yebob sdk
	private String session;
	private String token;
	private String rankingListId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ScrollView scrollView = new ScrollView(this);
		
		LinearLayout mainLayout = new LinearLayout(this);
		mainLayout.setOrientation(LinearLayout.HORIZONTAL);        
		
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);        
		scrollView.addView(layout);
		mainLayout.addView(scrollView);
		
		messageView = new TextView(getApplicationContext());
		messageView.setText("Please click the buttons");
		mainLayout.addView(messageView);

		setContentView(mainLayout);
		
		addAccessTokenButton(layout);
    	addLoginButton(layout);
    	addLogoutButton(layout);
    	addMeButton(layout);
    	addShareButton(layout);
    	addScoreSubmitButton(layout);
    	addRankingListsButton(layout);
    	addRankingTopsButton(layout);
    	addStatusGetButton(layout);
    	addStatusExistsButton(layout);
    	addInstallButton(layout);
	}
	
	// access_token
	private void addAccessTokenButton(LinearLayout layout) {
		Button btn = new Button(this);
		btn.setText("/access_token");
		layout.addView(btn);
		btn.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	Api.accessToken(APP_ID, APP_SECRET, new YBHandler(){
		    		@Override
					public void execute(JSONObject json) throws JSONException  {
		    			postAccessToken(json);
		    		}
		    		
		    		@Override
		    		public void onError(int code, String msg){
		    			defaultErrorHandler(code, msg);
		    		}
		    	});
		    }
		});
	}
	
	private void postAccessToken(JSONObject json) throws JSONException{
		token = json.getString("id");
		setMessageText(token);
	}
	
	// login
	private void addLoginButton(LinearLayout layout) {
		Button btn = new Button(this);
		btn.setText("/login");
		layout.addView(btn);
		btn.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	Api.login(token, SOCIAL_APPID, SOCIAL_APPCODE, "123", "http://alpha.yebob.com/TestYebobListRest/userlogin", new YBHandler(){
		    		@Override
					public void execute(JSONObject json) throws JSONException {
		    			postLogin(json);
		    		}
		    		
		    		@Override
		    		public void onError(int code, String msg){
		    			defaultErrorHandler(code, msg);
		    		}
		    	});
		    }
		});
	}

	private void postLogin(JSONObject json) throws JSONException {
		JSONObject obj = json.getJSONObject("session");
		session = obj.getString("id");
		setMessageText(session);
	}
	
	// logout
	private void addLogoutButton(LinearLayout layout) {
		Button btn = new Button(this);
		btn.setText("/logout");
		layout.addView(btn);
		btn.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	Api.logout("token", "session", new YBHandler(){
		    		@Override
					public void execute(JSONObject json) throws JSONException  {
		    			postLogout(json);
		    		}
		    		
		    		@Override
		    		public void onError(int code, String msg){
		    			defaultErrorHandler(code, msg);
		    		}
		    	});
		    }
		});
	}

	private void postLogout(JSONObject json) throws JSONException {
		setMessageText("done.");
	}
	
	// me
	private void addMeButton(LinearLayout layout) {
		Button btn = new Button(this);
		btn.setText("/me");
		layout.addView(btn);
		btn.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	Api.me(token, session, new YBHandler(){
		    		@Override
					public void execute(JSONObject json) throws JSONException  {
		    			postMe(json);
		    		}
		    		
		    		@Override
		    		public void onError(int code, String msg){
		    			defaultErrorHandler(code, msg);
		    		}
		    	});
		    }
		});
	}

	private void postMe(JSONObject json) throws JSONException {
		setMessageText(json.getString("community"));
	}
	
	// share
	private void addShareButton(LinearLayout layout) {
		Button btn = new Button(this);
		btn.setText("/share");
		layout.addView(btn);
		btn.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	Api.share(token, session, "text", null);
		    }
		});
	}

	// score submit
	private void addScoreSubmitButton(LinearLayout layout) {
		Button btn = new Button(this);
		btn.setText("/score/submit");
		layout.addView(btn);
		btn.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	Api.scoreSubmit(token, session, rankingListId, 100, null);
		    }
		});
	}

	// ranking lists
	private void addRankingListsButton(LinearLayout layout) {
		Button btn = new Button(this);
		btn.setText("/ranking/lists");
		layout.addView(btn);
		btn.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	Api.rankingLists(token, new YBHandler(){
		    		@Override
					public void execute(JSONObject json) throws JSONException  {
		    			postRankingLists(json);
		    		}
		    		
		    		@Override
		    		public void onError(int code, String msg){
		    			defaultErrorHandler(code, msg);
		    		}
		    	});
		    }
		});
	}

	private void postRankingLists(JSONObject json) throws JSONException {
		JSONObject list = (JSONObject) json.getJSONArray("lists").get(0);
		rankingListId = list.getString("id");
		setMessageText(String.format("id:%,s name:%s", rankingListId, list.getString("name")));
	}

	// ranking tops
	private void addRankingTopsButton(LinearLayout layout) {
		Button btn = new Button(this);
		btn.setText("/ranking/tops");
		layout.addView(btn);
		btn.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	int count = 30, start_row = 10;
		    	Api.rankingTops(token, rankingListId, count, start_row, "week", "friend", new YBHandler(){
		    		@Override
					public void execute(JSONObject json) throws JSONException  {
		    			postRankingTops(json);
		    		}
		    		
		    		@Override
		    		public void onError(int code, String msg){
		    			defaultErrorHandler(code, msg);
		    		}
		    	});
		    }
		});
	}

	private void postRankingTops(JSONObject json) throws JSONException {
		int total = json.getInt("total");
		JSONObject item = (JSONObject) json.getJSONArray("items").get(0);
		setMessageText(String.format("total:%d, item[0]: %d", total, item.getInt("score")));
	}

	// status get
	private void addStatusGetButton(LinearLayout layout) {
		Button btn = new Button(this);
		btn.setText("/status/get");
		layout.addView(btn);
		btn.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	Api.statusGet(token, new YBHandler(){
		    		@Override
					public void execute(JSONObject json) throws JSONException  {
		    			postStatusGet(json);
		    		}
		    		
		    		@Override
		    		public void onError(int code, String msg){
		    			defaultErrorHandler(code, msg);
		    		}
		    	});
		    }
		});
	}

	private void postStatusGet(JSONObject json) throws JSONException {
		setMessageText(String.format("%d", json.getInt("expires_in")));
	}
	
	// status exists
	private void addStatusExistsButton(LinearLayout layout) {
		Button btn = new Button(this);
		btn.setText("/status/exists");
		layout.addView(btn);
		btn.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	Api.statusExists(token, null);
		    }
		});
	}

	private void addInstallButton(LinearLayout layout) {
		Button btn = new Button(this);
		btn.setText("Install");
		layout.addView(btn);
		btn.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	Api.showDetail(getApplication(), "com.trackeen.sea");
		    }
		});
	}
	
	private void defaultErrorHandler(int code, String msg)
    {
		setMessageText(String.format("code: %d, msg: %s", code, msg));
    }
	
	private void setMessageText(String msg){
		messageView.setText(msg);
	}
}