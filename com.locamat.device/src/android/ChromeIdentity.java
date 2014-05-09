package com.locamat.device;

import java.io.IOException;
import java.lang.Exception;
import java.lang.Void;

import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
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



public class GetMacAdress extends CordovaPlugin {

    private static final String LOG_TAG = "ChromeIdentity";


    private CordovaArgs savedCordovaArgs;
    private CallbackContext savedCallbackContext;

    @Override
    public boolean execute(String action, CordovaArgs args, final CallbackContext callbackContext) throws JSONException {
        if ("getmac".equals(action)) {
            getMacAddress(callbackContext);
            return true;
        }

        return false;
    }


    public void getMacAddress(CallbackContext callbackContext) {
        WifiManager wimanager = (WifiManager) this.webView.getContext().getSystemService(Context.WIFI_SERVICE);
        String macAddress = wimanager.getConnectionInfo().getMacAddress();
        if (macAddress == null) {
            macAddress = "Device don't have mac address or wi-fi is disabled";
        }
        this.getAuthTokenCallback(macAddress, callbackContext);
    }

    private void getAuthTokenCallback(String macaddress, CallbackContext callbackContext) {
        if(macaddress.trim().equals("")) {
            callbackContext.error("Could not get auth token");
        } else {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("mac", macaddress);
                callbackContext.success(jsonObject);
            } catch (JSONException e) { }
        }
    }


}