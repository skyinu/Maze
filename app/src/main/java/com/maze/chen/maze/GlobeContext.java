package com.maze.chen.maze;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by chen on 2015/12/9.
 */
public class GlobeContext extends Application {
    private static final String PATH="score";
    private static final String KEY="history";
    public static SharedPreferences history;

    /**
     * 屏幕信息
     */
    public static int mScreenWidth;
    public static int mScreenHeight;
    public static int mContentHeight;
    /**
     * 当前关卡以及对应的行数和列数
     */
    public static int mGameRows;
    public static int mGameColumns;
    public static int pass;
    @Override
    public void onCreate() {
        super.onCreate();
        history=getSharedPreferences(PATH,MODE_PRIVATE);
        WindowManager windowManager= (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics=new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        mScreenWidth=displayMetrics.widthPixels;
        mScreenHeight=displayMetrics.heightPixels;
        pass=1;
    }

    /**
     * 更新历史成绩
     * @param score
     */
    public static void updateHistory(int score){
        int his=history.getInt(KEY+pass,0);
        if(score<his||his==0){
            SharedPreferences.Editor editor=history.edit();
            editor.putInt(KEY+pass,score);
            editor.commit();
        }
    }
    public static int getHistoryScore(int pass){
        return history.getInt(KEY+pass,0);
    }

    public static void nextPass(){
        mGameColumns=10+(pass-1)*2;
        int cellWidth=mScreenWidth/mGameColumns;
        mGameRows=mContentHeight/cellWidth;
    }
}
