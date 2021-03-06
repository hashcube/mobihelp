package com.tealeaf.plugin.plugins;

import com.tealeaf.logger;
import com.tealeaf.plugin.IPlugin;
import com.tealeaf.TeaLeaf;
import com.tealeaf.EventQueue;
import com.freshdesk.mobihelp.*;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.content.pm.PackageManager;

public class MobihelpPlugin implements IPlugin {

  private Activity mActivity;

  private final String TAG = "{mobihelp}";

  public class UnreadNotificationCountEvent extends com.tealeaf.event.Event {
    String statusCode;
    int count;

    public UnreadNotificationCountEvent(String status, int count) {
      super("mobihelpNotifCount");
      this.statusCode = status;
      this.count = count;
    }
  }

  public void onCreateApplication(Context applicationContext) {
  }

  public void onCreate(Activity activity, Bundle savedInstanceState) {
    this.mActivity = activity;
    PackageManager manager = activity.getBaseContext().getPackageManager();
    String domain, appId, appSecret, autoReply;
    int ratePromptCount;
    try {
      Bundle meta = manager.getApplicationInfo(activity.getApplicationContext().getPackageName(),
        PackageManager.GET_META_DATA).metaData;
      domain = meta.get("MOBIHELP_DOMAIN").toString();
      appId = meta.get("MOBIHELP_APP_ID").toString();
      appSecret = meta.get("MOBIHELP_APP_SECRET").toString();
      autoReply = meta.get("MOBIHELP_AUTO_REPLY_ENABLE").toString();
      ratePromptCount = Integer.parseInt(meta.get("MOBIHELP_RATE_PROMPT_COUNT").toString());
      MobihelpConfig config = new MobihelpConfig(domain, appId, appSecret);
      if ("true".equals(autoReply)) {
        config.setAutoReplyEnabled(true);
      } else {
        config.setAutoReplyEnabled(false);
      }
      if (ratePromptCount > 0) {
        config.setLaunchCountForReviewPrompt(ratePromptCount);
      }
      Mobihelp.init(activity, config);
    }
    catch (Exception e) {
      logger.log(TAG + "{exception}", "" + e.getMessage());
    }
  }

  public void onResume() {
  }

  public void onRenderResume() {
  }

  public void onStart() {
  }

  public void onFirstRun() {
  }

  public void onPause() {
  }

  public void onRenderPause() {
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
      logger.log(TAG + "{exception}", "" + e.getMessage());
    }
  }

  public void setUserEmail (String param) {
    JSONObject reqJson;
    try{
      reqJson = new JSONObject(param);
      this.setEmail(reqJson.getString("email"));
    }
    catch (Exception e){
      logger.log(TAG + "{exception}", "" + e.getMessage());
    }
  }

  public void setUserFullName (String param) {
    JSONObject reqJson;
    try{
      reqJson = new JSONObject(param);
      this.setFullName(reqJson.getString("full_name"));
    }
    catch (Exception e){
      logger.log(TAG + "{exception}", "" + e.getMessage());
    }
  }

  public void clearUserData(String param) {
    Mobihelp.clearUserData(this.mActivity);
  }

  public void leaveBreadCrumb (String param) {
    JSONObject reqJson;
    try{
      reqJson = new JSONObject(param);
      Mobihelp.leaveBreadCrumb(reqJson.getString("breadcrumb"));
    }
    catch (Exception e){
      logger.log(TAG + "{exception}", "" + e.getMessage());
    }
  }

  public void addCustomData (String param) {
    JSONObject reqJson;
    try{
      reqJson = new JSONObject(param);
      Mobihelp.addCustomData(reqJson.getString("field_name"), reqJson.getString("value"));
    }
    catch (Exception e){
      logger.log(TAG + "{exception}", "" + e.getMessage());
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

  public void getUnreadNotificationCount(String params) {
    int unread_count = 0;

    try {
      unread_count = Mobihelp.getUnreadCount(this.mActivity);
    }
    catch (Exception e) {
      logger.log(TAG + "{exception}", "" + e.getMessage());
    }

    EventQueue.pushEvent(new UnreadNotificationCountEvent("success", unread_count));
  }

  private void setEmail(String email) {
    Mobihelp.setUserEmail(this.mActivity, email);
  }

  private void setFullName(String fullName) {
    Mobihelp.setUserFullName(this.mActivity, fullName);
  }
}
