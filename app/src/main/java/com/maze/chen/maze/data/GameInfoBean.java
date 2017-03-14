package com.maze.chen.maze.data;

import com.maze.chen.maze.GlobeContext;
import com.maze.chen.maze.utils.GameUtil;

/**
 * Created by chen on 2017/3/12.
 */

public class GameInfoBean {
    //the maze view's height
    private int mContentHeight;
    //current game step
    private int mPass;
    public static int mGameRows;
    public static int mGameColumns;

    public GameInfoBean(){
        mPass = 1;
    }

    public void increasePass(){
        mGameColumns = 10+(mPass - 1) * 2;
        int cellWidth = GameUtil.getScreenWidth(GlobeContext.getAppContext()) / mGameColumns;
        mGameRows = mContentHeight/cellWidth;
    }


    public void setContentHeight(int mContentHeight) {
        this.mContentHeight = mContentHeight;
    }

    public void setPass(int mPass) {
        this.mPass = mPass;
    }

    public static void setGameRows(int mGameRows) {
        GameInfoBean.mGameRows = mGameRows;
    }

    public static void setGameColumns(int mGameColumns) {
        GameInfoBean.mGameColumns = mGameColumns;
    }

    public int getContentHeight() {
        return mContentHeight;
    }

    public int getPass() {
        return mPass;
    }

    public static int getGameRows() {
        return mGameRows;
    }

    public static int getGameColumns() {
        return mGameColumns;
    }
}
