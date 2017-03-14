package com.maze.chen.maze;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.maze.chen.maze.manager.UIManager;

/**
 * Created by chen on 2017/3/14.
 */

public class BaseActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        UIManager.getInstance().pushActivity(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        UIManager.getInstance().popActivity();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
