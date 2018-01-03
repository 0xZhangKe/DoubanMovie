package com.zhangke.doubanmovie.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;


/**
 * 用户权限相关工具
 * <p>
 * Created by ZhangKe on 2017/11/15.
 */

public class PermissionUtil {
    public static String[] PERMISSION = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    public static boolean isLacksOfPermission(Context context, String permission) {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) && ContextCompat.checkSelfPermission(
                context, permission) == PackageManager.PERMISSION_DENIED;
    }
}
