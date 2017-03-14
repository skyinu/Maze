package com.maze.chen.maze.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.sax.RootElement;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by chen on 2017/3/12.
 */

public class GameUtil {
    private static final String KEY_PATH="score";
    private static final String KEY_HISTORY_PREFIX="history";
    private static int SCREEN_WIDTH = -1;
    private static int SCREEN_HEIGHT = -1;
    public static void updateHistory(Context context,int pass, int score){
        SharedPreferences history = context.getSharedPreferences(KEY_PATH, Context.MODE_PRIVATE);
        int his=history.getInt(KEY_HISTORY_PREFIX + pass,0);
        if(score<his||his==0){
            SharedPreferences.Editor editor=history.edit();
            editor.putInt(KEY_HISTORY_PREFIX + pass,score);
            editor.commit();
        }
    }

    public static int getHistoryScore(Context context, int pass){
        SharedPreferences history = context.getSharedPreferences(KEY_PATH, Context.MODE_PRIVATE);
        return history.getInt(KEY_HISTORY_PREFIX + pass,0);
    }

    public static int getScreenWidth(Context context){
        if(SCREEN_WIDTH == -1){
            getScreenDimension(context);
        }
        return SCREEN_WIDTH;
    }

    public static int getScreenHeight(Context context){
        if(SCREEN_HEIGHT == -1){
            getScreenDimension(context);
        }
        return SCREEN_HEIGHT;
    }

    private static void getScreenDimension(Context context){
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        SCREEN_WIDTH = displayMetrics.widthPixels;
        SCREEN_HEIGHT = displayMetrics.heightPixels;
    }
}
