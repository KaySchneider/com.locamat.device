package com.locamat.device;

import java.io.IOException;
import java.lang.Exception;
import java.lang.String;
import java.lang.Void;


import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.LOG;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.accounts.Account;
import android.accounts.AccountManager;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.UserRecoverableNotifiedException;

public class GetMacAdress extends CordovaPlugin {

    private static final String LOG_TAG = "ChromeIdentity";
    private static final String WEB_CLIENT_ID = "insert-here-your-web-client-id-from: https://console.developers.google.com";

    private CordovaArgs savedCordovaArgs;
    private CallbackContext savedCallbackContext;

    @Override
    public boolean execute(String action, CordovaArgs args, final CallbackContext callbackContext) throws JSONException {
        String type;
        if ("getmac".equals(action)) {
            type = "mac";
            getMacAddress(callbackContext, type);
            return true;
        } else if("blindAuth".equals(action)) {
            type = "token";
            getToken(callbackContext, type);
            return true;
        }
        return false;
    }

    private final String[] getAccountNames() {
        AccountManager mAccountManager = AccountManager.get(this.webView.getContext());
        Account[] accounts = mAccountManager.getAccountsByType(
                GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
        String[] names = new String[accounts.length];
        for (int i = 0; i < names.length; i++) {
            Log.v(LOG_TAG, "inner loop names");
            Log.v(LOG_TAG, accounts[i].name);
            names[i] = accounts[i].name;
        }
        return names;
    }

    private void getToken(final CallbackContext callbackContext, String type) {
        final String typeInner = type;
        this.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {
                    String token = null;
                    String[] names = getAccountNames();
                    for(String name: names) {
                        Log.v(LOG_TAG, "try to get token for account name: "+ name);
                        try {
                            // android id 210274628941-6e8df5avlk4b32ja47kn251uvmjbp1iv.apps.googleusercontent.com
                            //       763426038-q0v78o2lanpuup8b1htepjo7lerpdch0.apps.googleusercontent.com
                            token = GoogleAuthUtil.getToken(webView.getContext(), name, "audience:server:client_id:" + WEB_CLIENT_ID);
                            //check if the token is null or false
                            //Log.v(LOG_TAG, "token for account " + name + " token: " + token );
                            if(token != null) {
                                //on every start i can use this token to authenticate an user to the backend!
                                getAuthTokenCallback(token, typeInner, callbackContext);
                                break;//stop the loop
                            }
                        } catch(Exception e) {
                            Log.v(LOG_TAG, "error get the token");
                        }
                        token = null;
                    }
                } catch(Exception exception) {
                    Log.v(LOG_TAG, "exception in calling token!");
                }
            }
        });
    }

    public void getMacAddress(CallbackContext callbackContext, String type) {
        WifiManager wimanager = (WifiManager) this.webView.getContext().getSystemService(Context.WIFI_SERVICE);
        String macAddress = wimanager.getConnectionInfo().getMacAddress();
        if (macAddress == null) {
            macAddress = "Device don't have mac address or wi-fi is disabled";
        }
        this.getAuthTokenCallback(macAddress, type, callbackContext);
    }

    /**
     *
     * @param macaddress
     * @param type  (string mac or token)
     * @param callbackContext
     */
    private final void getAuthTokenCallback(String macaddress, String type ,CallbackContext callbackContext) {
        if(macaddress.trim().equals("")) {
            callbackContext.error("Could not get auth token");
        } else {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(type, macaddress);
                callbackContext.success(jsonObject);
            } catch (JSONException e) {

            }
        }
    }


}