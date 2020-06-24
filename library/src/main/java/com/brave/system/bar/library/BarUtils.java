package com.brave.system.bar.library;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * <pre>
 *     <b>author</b>    ：BraveTou
 *     <b>blog</b>      ：https://blog.csdn.net/bravetou
 *     <b>time</b>      ：2020/6/23 11:30
 *     <b>desc</b>      ：<pre>
 *         系统栏（状态栏、虚拟按键栏）工具类，
 *         与{@link StatusBarUtils}类和{@link NavBarUtils}类互斥，
 *         若只需要设置状态栏，
 *         请使用{@link StatusBarUtils}类，
 *         若只需要设置虚拟按键栏，
 *         请使用{@link NavBarUtils}类，
 *         如需要其他种类系统栏，
 *         请使用{@linkplain #setSystemBarColor(Window, View, boolean, boolean, boolean, boolean, boolean, boolean, int, int)}方法，
 *         它是所有操作系统栏的源方法
 *     </pre>
 * </pre>
 */
public final class BarUtils {
    // 状态栏Tag
    private static final String STATUS_BAR_TAG = "COM_BRAVE_SYSTEM_BAR_LIBRARY_STATUS_BAR";
    // 虚拟按键栏Tag
    private static final String NAV_BAR_TAG = "COM_BRAVE_SYSTEM_BAR_LIBRARY_NAV_BAR";

    /**
     * 获取（创建）系统栏（状态栏、虚拟按键栏）
     *
     * @param isStatusBar 是状态栏
     */
    private static final View getBar(@NonNull Window window, boolean isStatusBar) {
        // Android 5.0 + 不需要创建
        if (isAndroid_5_0_Above()) {
            return null;
        }
        // 获取装饰布局
        ViewGroup decorView = (ViewGroup) window.getDecorView();
        // 获取Bar View Tag
        String tag = isStatusBar ? STATUS_BAR_TAG : NAV_BAR_TAG;
        // 根据Tag获取Bar View
        View bar = decorView.findViewWithTag(tag);
        // Bar View为空
        if (null == bar) {
            // 创建一个新的Bar View
            bar = new View(window.getContext());
            // 获取Context
            Context context = window.getContext();
            // 获取 Bar 高度
            int barHeight = isStatusBar
                    ? Utils.getStatusBarHeight(context)
                    : Utils.getNavBarHeight(context);
            // 创建一个新的布局属性，并把Bar高度赋值给它
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, barHeight);
            // 设置布局位置居于顶部
            params.gravity = Gravity.TOP;
            // 把布局属性赋值给Bar View
            bar.setLayoutParams(params);
            // 设置Bar View Tag
            bar.setTag(tag);
            // 把Bar View添加到装饰布局
            decorView.addView(bar);
        }
        // Bar View
        return bar;
    }

    /**
     * 获取状态栏
     */
    private static final View getStatusBar(@NonNull Window window) {
        return getBar(window, true);
    }

    /**
     * 获取虚拟按键栏
     */
    private static final View getNavBar(@NonNull Window window) {
        return getBar(window, false);
    }

    /**
     * 设置根布局参数
     */
    private static final void setRootView(@NonNull View rootView,
                                          boolean fitSystemWindows,
                                          boolean clipToPadding) {
        if (null != rootView) {
            if (rootView instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) rootView;
                // 是否需要考虑系统栏占据的区域来显示
                // true的情况下
                // 内容不会被系统栏遮挡
                viewGroup.setFitsSystemWindows(fitSystemWindows);
                // 绘制区域是否在padding里面
                // false的情况下
                // 子View不受Padding的影响，可以展示在Padding的区域
                viewGroup.setClipToPadding(clipToPadding);
            }
        }
    }

    /**
     * 设置window标记
     *
     * @param window         窗口
     * @param isStatusBar    是状态栏
     * @param isNavBar       是虚拟按键栏
     * @param isBlack        状态栏深色字体
     * @param preventShaking 防止抖动
     */
    private static final void setFlags(@NonNull Window window,
                                       boolean isStatusBar,
                                       boolean isNavBar,
                                       boolean isBlack,
                                       boolean preventShaking) {
        // Android 5.0 +
        if (isAndroid_5_0_Above()) {
            // 添加绘制系统栏的背景的标志
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // 状态栏
            if (isStatusBar) {
                // 清除透明状态栏的标志
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            // 虚拟按键栏
            if (isNavBar) {
                // 清除透明虚拟按键栏的标志
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
            // 防止全屏显示时，弹出状态栏或者虚拟按键栏屏幕抖动
            if (preventShaking) {
                // 全屏显示，但状态栏不会被隐藏覆盖，状态栏依然可见，Activity 顶端布局部分会被状态遮住
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // 防止系统栏隐藏时内容区域大小发生变化
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                // 是虚拟按键栏
                if (isNavBar) {
                    // 隐藏虚拟按键栏
                    option = option | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
                }
                // 状态栏深色字体
                if (isBlack) {
                    // 当前设备API大于等于23
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        // 请求状态栏深色字体
                        option = option | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                    }
                } else {
                    // 设置系统栏UI可见
                    option = option | View.SYSTEM_UI_FLAG_VISIBLE;
                }
                // 请求改变状态栏或其他屏幕/窗口装饰的可见性
                window.getDecorView().setSystemUiVisibility(option);
            } else {
                // 状态栏深色字体
                if (isBlack) {
                    // 当前设备API大于等于23
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        // 请求状态栏深色字体
                        // 请求改变状态栏或其他屏幕/窗口装饰的可见性
                        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                    }
                }
            }
        }
        // Android 4.4 +
        else if (isAndroid_4_4_Above()) {
            // 是状态栏
            if (isStatusBar) {
                // 添加透明状态栏标记
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            // 是虚拟按键栏
            if (isNavBar) {
                // 添加透明虚拟按键栏标记
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
        }
    }

    /**
     * Android 4.4+
     */
    private static final boolean isAndroid_4_4_Above() {
        // 当前设备API大于等于19
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    /**
     * Android 5.0+
     */
    private static final boolean isAndroid_5_0_Above() {
        // 当前设备API大于等于21
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * 设置系统栏颜色
     *
     * @param window           窗口
     * @param rootView         根布局
     * @param isStatusBar      状态栏
     * @param isNavBar         虚拟按键栏
     * @param preventShaking   防止抖动
     * @param isBlack          深色字体
     * @param fitSystemWindows 系统栏占位
     * @param clipToPadding    子View不能展示在Padding区域
     * @param color            颜色
     * @param alpha            透明度[0,255]，值越小越透明
     */
    public static final void setSystemBarColor(@NonNull Window window,
                                               @NonNull View rootView,
                                               boolean isStatusBar,
                                               boolean isNavBar,
                                               boolean preventShaking,
                                               boolean isBlack,
                                               boolean fitSystemWindows,
                                               boolean clipToPadding,
                                               @ColorInt int color,
                                               @IntRange(from = 0, to = 255) int alpha) {
        if (!isStatusBar && !isNavBar) {
            return;
        }
        if (!isAndroid_4_4_Above()) {
            return;
        }
        // 获取是否拥有虚拟按键栏
        boolean hasNavigationBar = Utils.hasNavigationBar(window);
        isNavBar = isNavBar && hasNavigationBar;
        // 设置标识
        setFlags(window, isStatusBar, isNavBar, isBlack, preventShaking);
        // 计算系统栏背景颜色
        int barColor = calculateColor(color, alpha);
        // 需要更改状态栏颜色
        if (isStatusBar) {
            if (isAndroid_5_0_Above()) {
                window.setStatusBarColor(barColor);
            } else if (isAndroid_4_4_Above()) {
                View statusBar = getStatusBar(window);
                if (statusBar.getVisibility() != View.VISIBLE) {
                    statusBar.setVisibility(View.VISIBLE);
                }
                statusBar.setBackgroundColor(barColor);
            }
        }
        // 需要更改虚拟按键栏颜色
        if (isNavBar) {
            if (isAndroid_5_0_Above()) {
                window.setNavigationBarColor(barColor);
            } else if (isAndroid_4_4_Above()) {
                View navBar = getNavBar(window);
                if (navBar.getVisibility() != View.VISIBLE) {
                    navBar.setVisibility(View.VISIBLE);
                }
                navBar.setBackgroundColor(barColor);
            }
        }
        // 设置根布局参数
        setRootView(rootView, fitSystemWindows, clipToPadding);
    }

    /**
     * 设置系统栏颜色
     *
     * @param window           窗口
     * @param rootView         根布局
     * @param isStatusBar      状态栏
     * @param isNavBar         虚拟按键栏
     * @param isBlack          深色字体
     * @param fitSystemWindows 系统栏占位
     * @param clipToPadding    子View不能展示在Padding区域
     * @param color            颜色
     * @param alpha            透明度[0,255]，值越小越透明
     */
    public static final void setSystemBarColor(@NonNull Window window,
                                               @NonNull View rootView,
                                               boolean isStatusBar,
                                               boolean isNavBar,
                                               boolean isBlack,
                                               boolean fitSystemWindows,
                                               boolean clipToPadding,
                                               @ColorInt int color,
                                               @IntRange(from = 0, to = 255) int alpha) {
        setSystemBarColor(window,
                rootView,
                isStatusBar,
                isNavBar,
                true,
                isBlack,
                fitSystemWindows,
                clipToPadding,
                color,
                alpha);
    }

    /**
     * 设置系统栏颜色
     *
     * @param window           窗口
     * @param rootView         根布局
     * @param isStatusBar      状态栏
     * @param isNavBar         虚拟按键栏
     * @param isBlack          深色字体
     * @param fitSystemWindows 系统栏占位
     * @param clipToPadding    子View不能展示在Padding区域
     * @param color            颜色
     * @param alpha            透明度[0.0,1.0]，值越小越透明
     */
    public static final void setSystemBarColor(@NonNull Window window,
                                               @NonNull View rootView,
                                               boolean isStatusBar,
                                               boolean isNavBar,
                                               boolean isBlack,
                                               boolean fitSystemWindows,
                                               boolean clipToPadding,
                                               @ColorInt int color,
                                               @FloatRange(from = 0, to = 1) float alpha) {
        setSystemBarColor(window,
                rootView,
                isStatusBar,
                isNavBar,
                isBlack,
                fitSystemWindows,
                clipToPadding,
                color,
                ((int) (alpha * 255)));
    }

    /**
     * 设置系统栏颜色（默认系统栏占位，子View可以展示在Padding区域）
     *
     * @param window      窗口
     * @param rootView    根布局
     * @param isStatusBar 状态栏
     * @param isNavBar    虚拟按键栏
     * @param isBlack     深色字体
     * @param color       颜色
     */
    public static final void setSystemBarColor(@NonNull Window window,
                                               @NonNull View rootView,
                                               boolean isStatusBar,
                                               boolean isNavBar,
                                               boolean isBlack,
                                               @ColorInt int color) {
        setSystemBarColor(window,
                rootView,
                isStatusBar,
                isNavBar,
                isBlack,
                true,
                true,
                color,
                255);
    }

    /**
     * 设置系统栏颜色（默认系统栏占位，子View可以展示在Padding区域，设置状态栏与虚拟按键栏）
     *
     * @param window   窗口
     * @param rootView 根布局
     * @param isBlack  深色字体
     * @param color    颜色
     */
    public static final void setSystemBarColor(@NonNull Window window,
                                               @NonNull View rootView,
                                               boolean isBlack,
                                               @ColorInt int color) {
        setSystemBarColor(window,
                rootView,
                true,
                true,
                isBlack,
                color);
    }

    /**
     * 设置系统栏颜色（默认系统栏占位，子View可以展示在Padding区域，设置状态栏与虚拟按键栏，状态栏字体浅色模式）
     *
     * @param window   窗口
     * @param rootView 根布局
     * @param color    颜色
     */
    public static final void setSystemBarColor(@NonNull Window window,
                                               @NonNull View rootView,
                                               @ColorInt int color) {
        setSystemBarColor(window,
                rootView,
                false,
                color);
    }

    /**
     * 设置系统栏颜色
     *
     * @param activity         活动
     * @param isStatusBar      状态栏
     * @param isNavBar         虚拟按键栏
     * @param isBlack          深色字体
     * @param fitSystemWindows 系统栏占位
     * @param clipToPadding    子View不能展示在Padding区域
     * @param color            颜色
     * @param alpha            透明度[0,255]，值越小越透明
     */
    public static final void setSystemBarColor(@NonNull Activity activity,
                                               boolean isStatusBar,
                                               boolean isNavBar,
                                               boolean isBlack,
                                               boolean fitSystemWindows,
                                               boolean clipToPadding,
                                               @ColorInt int color,
                                               @IntRange(from = 0, to = 255) int alpha) {
        ViewGroup group = activity.findViewById(android.R.id.content);
        if (group.getChildCount() < 1) {
            return;
        }
        setSystemBarColor(activity.getWindow(),
                group.getChildAt(0),
                isStatusBar,
                isNavBar,
                isBlack,
                fitSystemWindows,
                clipToPadding,
                color,
                alpha);
    }

    /**
     * 设置系统栏颜色
     *
     * @param activity         活动
     * @param isStatusBar      状态栏
     * @param isNavBar         虚拟按键栏
     * @param isBlack          深色字体
     * @param fitSystemWindows 系统栏占位
     * @param clipToPadding    子View不能展示在Padding区域
     * @param color            颜色
     * @param alpha            透明度[0.0,1.0]，值越小越透明
     */
    public static final void setSystemBarColor(@NonNull Activity activity,
                                               boolean isStatusBar,
                                               boolean isNavBar,
                                               boolean isBlack,
                                               boolean fitSystemWindows,
                                               boolean clipToPadding,
                                               @ColorInt int color,
                                               @FloatRange(from = 0, to = 1) float alpha) {
        setSystemBarColor(activity,
                isStatusBar,
                isNavBar,
                isBlack,
                fitSystemWindows,
                clipToPadding,
                color,
                ((int) (alpha * 255)));
    }

    /**
     * 设置系统栏颜色（默认系统栏占位，子View可以展示在Padding区域）
     *
     * @param activity    活动
     * @param isStatusBar 状态栏
     * @param isNavBar    虚拟按键栏
     * @param isBlack     深色字体
     * @param color       颜色
     */
    public static final void setSystemBarColor(@NonNull Activity activity,
                                               boolean isStatusBar,
                                               boolean isNavBar,
                                               boolean isBlack,
                                               @ColorInt int color) {
        setSystemBarColor(activity,
                isStatusBar,
                isNavBar,
                isBlack,
                true,
                true,
                color,
                255);
    }

    /**
     * 设置系统栏颜色（默认系统栏占位，子View可以展示在Padding区域，设置状态栏与虚拟按键栏）
     *
     * @param activity 活动
     * @param isBlack  深色字体
     * @param color    颜色
     */
    public static final void setSystemBarColor(@NonNull Activity activity,
                                               boolean isBlack,
                                               @ColorInt int color) {
        setSystemBarColor(activity,
                true,
                true,
                isBlack,
                color);
    }

    /**
     * 设置系统栏颜色（默认系统栏占位，子View可以展示在Padding区域，设置状态栏与虚拟按键栏，状态栏字体浅色模式）
     *
     * @param activity 活动
     * @param color    颜色
     */
    public static final void setSystemBarColor(@NonNull Activity activity,
                                               @ColorInt int color) {
        setSystemBarColor(activity,
                false,
                color);
    }

    /**
     * 计算颜色
     *
     * @param color color值
     * @param alpha alpha值
     */
    private static final @ColorInt
    int calculateColor(@ColorInt int color,
                       @IntRange(from = 0, to = 255) int alpha) {
        return Color.argb(alpha,
                color >> 16 & 0xff,
                color >> 8 & 0xff,
                color & 0xff);
    }

    /**
     * 修改 MIUI V6  以上状态栏字体颜色
     */
    public static final void setMIUIStatusBarDarkIcon(@NonNull Activity activity, boolean darkIcon) {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            int darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkIcon ? darkModeFlag : 0, darkModeFlag);
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    /**
     * 修改魅族状态栏字体颜色 Flyme 4.0
     */
    public static final void setMeizuStatusBarDarkIcon(@NonNull Activity activity, boolean darkIcon) {
        try {
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
            Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
            Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
            darkFlag.setAccessible(true);
            meizuFlags.setAccessible(true);
            int bit = darkFlag.getInt(null);
            int value = meizuFlags.getInt(lp);
            if (darkIcon) {
                value |= bit;
            } else {
                value &= ~bit;
            }
            meizuFlags.setInt(lp, value);
            activity.getWindow().setAttributes(lp);
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 设置系统栏半透明
     */
    public static final void setTranslucent(@NonNull Window window,
                                            @NonNull View rootView) {
        setSystemBarColor(window,
                rootView,
                true,
                true,
                false,
                true,
                true,
                Color.BLACK,
                112);
    }

    /**
     * 设置系统栏半透明
     */
    public static final void setTranslucent(@NonNull Activity activity) {
        setSystemBarColor(activity,
                true,
                true,
                false,
                true,
                true,
                Color.BLACK,
                112);
    }

    /**
     * 设置系统栏透明
     */
    public static final void setTransparent(@NonNull Window window,
                                            @NonNull View rootView) {
        setSystemBarColor(window,
                rootView,
                true,
                true,
                false,
                true,
                true,
                Color.BLACK,
                0);
    }

    /**
     * 设置系统栏透明
     */
    public static final void setTransparent(@NonNull Activity activity) {
        setSystemBarColor(activity,
                true,
                true,
                false,
                true,
                true,
                Color.BLACK,
                0);
    }
}