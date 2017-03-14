package com.maze.chen.maze.manager;

import com.maze.chen.maze.BaseActivity;

import java.util.Stack;

/**
 * Created by chen on 2017/3/14.
 */

public class UIManager {
    private static UIManager mInstance = new UIManager();
    private volatile Stack<BaseActivity> mActivityStack;
    private UIManager(){
        mActivityStack = new Stack<>();
    }

    public static UIManager getInstance(){
        return mInstance;
    }

    public void pushActivity(BaseActivity baseActivity){
        mActivityStack.push(baseActivity);
    }

    public BaseActivity popActivity(){
        return mActivityStack.pop();
    }

    public BaseActivity getTopActivity(){
        return mActivityStack.peek();
    }

}
