package com.brave.system.bar.utils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.brave.system.bar.library.BarUtils;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BarUtils.setSystemBarColor(this,
                true,
                true,
                false,
                true,
                true,
                Color.BLUE,
                255);
        setRootView(this);
    }

    /**
     * 设置根布局参数
     */
    private void setRootView(Activity activity) {
        ViewGroup parent = (ViewGroup) activity.findViewById(android.R.id.content);
        Log.d(TAG, "setRootView: parent=" + parent);
        for (int i = 0, count = parent.getChildCount(); i < count; i++) {
            View childView = parent.getChildAt(i);
            Log.d(TAG, "setRootView: child=" + childView);
        }
    }
}
