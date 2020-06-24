package com.brave.system.bar.library;

import android.app.Activity;
import android.view.View;
import android.view.Window;

import androidx.annotation.ColorInt;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

/**
 * <pre>
 *     <b>author</b>    ：BraveTou
 *     <b>blog</b>      ：https://blog.csdn.net/bravetou
 *     <b>time</b>      ：2020/6/23 11:33
 *     <b>desc</b>      ：<pre>
 *         状态栏工具类，
 *         与{@link BarUtils}类和{@link NavBarUtils}类互斥，
 *         若需要同时设置状态栏和虚拟按键栏，
 *         请使用{@link BarUtils}类
 *     </pre>
 * </pre>
 */
public final class StatusBarUtils {
    /**
     * 设置状态栏颜色
     *
     * @param window           窗口
     * @param rootView         根布局
     * @param isBlack          深色字体
     * @param fitSystemWindows 系统栏占位
     * @param clipToPadding    子View不能展示在Padding区域
     * @param color            颜色
     * @param alpha            透明度[0,255]，值越小越透明
     */
    public static final void setColor(@NonNull Window window,
                                      @NonNull View rootView,
                                      boolean isBlack,
                                      boolean fitSystemWindows,
                                      boolean clipToPadding,
                                      @ColorInt int color,
                                      @IntRange(from = 0, to = 255) int alpha) {
        BarUtils.setSystemBarColor(window,
                rootView,
                true,
                false,
                isBlack,
                fitSystemWindows,
                clipToPadding,
                color,
                alpha);
    }

    /**
     * 设置状态栏颜色（默认状态栏占位，子View可以展示在Padding区域）
     *
     * @param window   窗口
     * @param rootView 根布局
     * @param isBlack  深色字体
     * @param color    颜色
     */
    public static final void setColor(@NonNull Window window,
                                      @NonNull View rootView,
                                      boolean isBlack,
                                      @ColorInt int color) {
        setColor(window,
                rootView,
                isBlack,
                true,
                true,
                color,
                255);
    }

    /**
     * 设置状态栏颜色（默认状态栏占位，子View可以展示在Padding区域，状态栏字体浅色模式）
     *
     * @param window   窗口
     * @param rootView 根布局
     * @param color    颜色
     */
    public static final void setColor(@NonNull Window window,
                                      @NonNull View rootView,
                                      @ColorInt int color) {
        setColor(window,
                rootView,
                false,
                color);
    }

    /**
     * 设置状态栏颜色
     *
     * @param activity         活动
     * @param isBlack          深色字体
     * @param fitSystemWindows 系统栏占位
     * @param clipToPadding    子View不能展示在Padding区域
     * @param color            颜色
     * @param alpha            透明度[0,255]，值越小越透明
     */
    public static final void setColor(@NonNull Activity activity,
                                      boolean isBlack,
                                      boolean fitSystemWindows,
                                      boolean clipToPadding,
                                      @ColorInt int color,
                                      @IntRange(from = 0, to = 255) int alpha) {
        BarUtils.setSystemBarColor(activity,
                true,
                false,
                isBlack,
                fitSystemWindows,
                clipToPadding,
                color,
                alpha);
    }

    /**
     * 设置状态栏颜色（默认状态栏占位，子View可以展示在Padding区域）
     *
     * @param activity 活动
     * @param isBlack  深色字体
     * @param color    颜色
     */
    public static final void setColor(@NonNull Activity activity,
                                      boolean isBlack,
                                      @ColorInt int color) {
        setColor(activity,
                isBlack,
                true,
                true,
                color,
                255);
    }

    /**
     * 设置状态栏颜色（默认状态栏占位，子View可以展示在Padding区域，状态栏字体浅色模式）
     *
     * @param activity 活动
     * @param color    颜色
     */
    public static final void setColor(@NonNull Activity activity,
                                      @ColorInt int color) {
        setColor(activity,
                false,
                color);
    }
}