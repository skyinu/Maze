package com.maze.chen.maze;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.maze.chen.maze.utils.GameUtil;

/**
 * Created by chen on 2015/12/9.
 */
public class GlobeContext extends Application {
    /**
     * 屏幕信息
     */
    public static int mContentHeight;
    /**
     * 当前关卡以及对应的行数和列数
     */
    public static int mGameRows;
    public static int mGameColumns;
    public static int pass;

    private static Context mAppContext;

    public static Context getAppContext(){
        return mAppContext;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext  = this;
        pass=1;
    }

    public static void nextPass(){
        mGameColumns=10+(pass-1)*2;
        int cellWidth= GameUtil.getScreenWidth(GlobeContext.getAppContext())/mGameColumns;
        mGameRows=mContentHeight/cellWidth;
    }
}
