package com.evil.check;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

/**
 * @项目名： xiaofang
 * @包名： com.xiaofang.controlsystem.utils
 * @创建者: Noah.冯
 * @时间: 18:38
 * @描述： TODO
 */

public class ManifestUtils {
    public static String getManifestString(Context context,String key) {
        String keyString = "";
        try {
            PackageManager packageManager = context.getPackageManager();
            String appPackageName = context.getPackageName();
            ApplicationInfo appInfo = packageManager.getApplicationInfo(appPackageName, PackageManager.GET_META_DATA);
            keyString = appInfo.metaData.getString(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return keyString;
    }
}
