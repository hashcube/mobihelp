package com.tealeaf.plugin.plugins;

import com.tealeaf.logger;
import com.tealeaf.plugin.IPlugin;
import com.tealeaf.TeaLeaf;
import com.tealeaf.EventQueue;
import com.freshdesk.mobihelp.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.util.Log;
import android.os.Bundle;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;

public class MobihelpPlugin implements IPlugin {

	private Activity mActivity;
	
	public class UnreadNotificationCountEvent extends com.tealeaf.event.Event {
		String statusCode;
		int count;

		public UnreadNotificationCountEvent(String status, int count) {
			super("unreadNotificationCount");
			this.statusCode = status;
			this.count = count;			
		}		
	}

	private UnreadUpdatesCallback countUpdateCallback = new UnreadUpdatesCallback() {
		String status;
		
		@Override
		public void onResult(MobihelpCallbackStatus statusCode, Integer count) {
			if(statusCode == MobihelpCallbackStatus.STATUS_SUCCESS) {
				status = "success";
			}
			else if(statusCode == MobihelpCallbackStatus.STATUS_NO_TICKETS_CREATED) {
				status = "noTicket";
			}
			else if(statusCode == MobihelpCallbackStatus.STATUS_NO_NETWORK_CONNECTION) {
				status = "noNetwork";
			}
			else if(statusCode == MobihelpCallbackStatus.STATUS_UNKNOWN) {
				status = "unknown";
			}
			else{
				status = "error";
			}
			android.util.Log.d("UnreadCountDebug", "asynchronous call");
			EventQueue.pushEvent(new UnreadNotificationCountEvent(status, count));
		}
	};
	
	public void onCreateApplication(Context applicationContext) {
	}

	public void onCreate(Activity activity, Bundle savedInstanceState) {
		this.mActivity = activity;
		PackageManager manager = activity.getBaseContext().getPackageManager();
		String domain, appId, appSecret;
		try {
			Bundle meta = manager.getApplicationInfo(activity.getApplicationContext().getPackageName(),
				PackageManager.GET_META_DATA).metaData;
			domain = meta.get("MOBIHELP_DOMAIN").toString();
			appId = meta.get("MOBIHELP_APP_ID").toString();
			appSecret = meta.get("MOBIHELP_APP_SECRET").toString();
			MobihelpConfig config = new MobihelpConfig(domain, appId, appSecret);
			Mobihelp.init(activity, config);
		}
		catch (Exception e) {
			android.util.Log.d("EXCEPTION", "" + e.getMessage());
		}
	}
	
	public void onResume() {		
	}
	
	public void onStart() {		
	}
	
	public void onPause() {
	}

	public void onStop() {
	}

	public void onDestroy() {
	}

	public void onNewIntent(Intent intent) {
	}

	public void setInstallReferrer(String referrer) {
	}

	public void onActivityResult(Integer request, Integer result, Intent data) {
	}

	public boolean consumeOnBackPressed() {
		return true;
	}

	public void onBackPressed() {
	}
	
	public void setUserInfo(String userInfo) {
		try {
			JSONObject userObj = new JSONObject(userInfo);
			this.setFullName((String) userObj.get("full_name"));
			this.setEmail((String) userObj.get("email"));
		}
		catch (Exception e) {
			android.util.Log.d("EXCEPTION", "" + e.getMessage());
		}
	}
	
	public void setUserEmail (String param) {
		JSONObject reqJson;		
		try{
			reqJson = new JSONObject(param);
			this.setEmail(reqJson.getString("email"));
		}
		catch (Exception e){
			android.util.Log.d("EXCEPTION", "" + e.getMessage());
		}		
	}

	public void setUserFullName (String param) {
		JSONObject reqJson;		
		try{
			reqJson = new JSONObject(param);
			this.setFullName(reqJson.getString("full_name"));
		}
		catch (Exception e){
			android.util.Log.d("EXCEPTION", "" + e.getMessage());
		}		
	}

	public void clearUserData(String param) {
		Mobihelp.clearUserData(this.mActivity);
	}
	
	public void addBreadCrumb (String param) {
		JSONObject reqJson;		
		try{
			reqJson = new JSONObject(param);
			Mobihelp.leaveBreadCrumb(reqJson.getString("breadcrumb_text"));
		}
		catch (Exception e){
			android.util.Log.d("EXCEPTION", "" + e.getMessage());
		}		
	}

	public void addCustomData (String param) {
		JSONObject reqJson;		
		try{
			reqJson = new JSONObject(param);
			Mobihelp.addCustomData(reqJson.getString("field_name"), reqJson.getString("value"));
		}
		catch (Exception e){
			android.util.Log.d("EXCEPTION", "" + e.getMessage());
		}		
	}
	
	public void showAppRateDialog(String params) {
		Mobihelp.showAppRateDialog(this.mActivity);
	}
	
	public void showConversations(String params) {
		Mobihelp.showConversations(this.mActivity);
	}
	
	public void showFeedback(String params) {
		Mobihelp.showFeedback(this.mActivity);
	}
	
	public void showSupport(String params) {
		Mobihelp.showSupport(this.mActivity);
	}
	
	public void checkUnreadNotifications(String params) {
		JSONObject reqJson;
		boolean async;
		int count;
		try{
			reqJson = new JSONObject(params);
			async = reqJson.getBoolean("async");
			if(async) {
				Mobihelp.getUnreadCountAsync(this.mActivity, countUpdateCallback);
			}
			else {
				count = Mobihelp.getUnreadCount(this.mActivity);
				android.util.Log.d("UnreadCountDebug", "synchronous call");
				EventQueue.pushEvent(new UnreadNotificationCountEvent("success", count));
			}
		}
		catch (Exception e){
			android.util.Log.d("EXCEPTION", "" + e.getMessage());
		}		
		
	}
	
	private void setEmail(String email) {
		Mobihelp.setUserEmail(this.mActivity, email);
	}
	
	private void setFullName(String fullName) {
		Mobihelp.setUserFullName(this.mActivity, fullName);
	}

}
