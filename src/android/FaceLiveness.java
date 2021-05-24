package com.face.liveness;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PermissionHelper;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

import nodeflux.sdk.liveness.Liveness;


/**
 * This class echoes a string called from JavaScript.
 */
public class FaceLiveness extends CordovaPlugin {

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (!isCameraPermissionGranted()) {
            requestCameraPermission();
        } else {
            if (action.equals("liveness")) {
                Intent intent = new Intent(cordova.getActivity(), Liveness.class);
                intent.putExtra("ACCESS_KEY", "{ACCESS_KEY}");
                intent.putExtra("SECRET_KEY", "{SECRET_KEY}");
                intent.putExtra("THRESHOLD", 0.7);

                cordova.startActivityForResult(this, intent, 10);
                Liveness.setUpListener(new Liveness.LivenessCallback() {

                    @Override
                    public void onSuccess(boolean isLive, Bitmap bitmap, double livenessScore) {
                        callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK));
                    }

                    @Override
                    public void onError(String message) {
                        callbackContext.error(message);
                    }
                });
                return true;
            }
        }
        return false;
    }

    public void requestCameraPermission() {
        PermissionHelper.requestPermission(this, 1, Manifest.permission.CAMERA);
    }

    public boolean isCameraPermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PermissionHelper.hasPermission(this, Manifest.permission.CAMERA)) {
                Log.v("permission", "Camera permission is granted");
                return true;
            } else {
                Log.v("permission", "Camera permission is denied");
                return false;
            }
        } else {
            Log.v("Permission", "Camera Permission is granted");
            return true;
        }
    }
}
