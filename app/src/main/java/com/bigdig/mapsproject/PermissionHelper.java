package com.bigdig.mapsproject;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

public class PermissionHelper {

    public interface IPermissionCallback {
        void onPermissionSuccess();
    }

    private static String[] permissionList = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private static final int PERMISSION_REQUEST_CODE = 324;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void getPermissions(Activity context, IPermissionCallback callback) {
        boolean isGranted = true;
        for (String permission : permissionList) {
            isGranted &= context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        }
        if (isGranted)
            callback.onPermissionSuccess();
        else
            requestPermissions(context);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private static void requestPermissions(Activity context) {
        context.requestPermissions(permissionList, PERMISSION_REQUEST_CODE);
    }

    public static void onRequestPermissionResult(Activity activity, IPermissionCallback callback, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean allGranted = true;
            for (int result : grantResults) {
                allGranted &= result == PackageManager.PERMISSION_GRANTED;
            }
            if(allGranted){
                callback.onPermissionSuccess();
            } else {
                activity.finish();
            }
        }
    }
}
