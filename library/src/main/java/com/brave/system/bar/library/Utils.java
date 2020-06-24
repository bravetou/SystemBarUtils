package com.brave.system.bar.library;

import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

/**
 * <pre>
 *     <b>author</b>    ：BraveTou
 *     <b>blog</b>      ：https://blog.csdn.net/bravetou
 *     <b>time</b>      ：2020/6/23 11:43
 *     <b>desc</b>      ：<pre>
 *         工具类
 *     </pre>
 * </pre>
 */
final class Utils {
    /**
     * 获取状态栏的高度
     */
    static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier(
                "status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获取底部虚拟按键栏的高度
     */
    static int getNavBarHeight(Context context) {
        int result = 0;
        // 判断底部虚拟按键栏是否显示
        int rid = context.getResources().getIdentifier(
                "config_showNavigationBar", "bool", "android");
        if (rid != 0) {
            int resourceId = context.getResources().getIdentifier(
                    "navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = context.getResources().getDimensionPixelSize(resourceId);
            }
        }
        return result;
    }

    /**
     * 是否拥有底部虚拟按键栏
     */
    static boolean hasNavigationBar(@NonNull Window window) {
        WindowManager windowManager = window.getWindowManager();
        Display d = windowManager.getDefaultDisplay();
        // 获取整个屏幕的高度
        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            d.getRealMetrics(realDisplayMetrics);
        } else {
            return false;
        }
        // 整个屏幕的
        int realHeight = realDisplayMetrics.heightPixels;
        int realWidth = realDisplayMetrics.widthPixels;
        // 获取内容展示部分的高度
        DisplayMetrics displayMetrics = new DisplayMetrics();
        d.getMetrics(displayMetrics);
        // 内容展示部分的
        int displayHeight = displayMetrics.heightPixels;
        int displayWidth = displayMetrics.widthPixels;
        // 是否拥有底部虚拟按键栏
        return (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;
    }
}