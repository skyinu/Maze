package com.maze.chen.maze;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by chen on 2016/3/9.
 */
public class WelComeActivity extends BaseActivity implements View.OnClickListener{
    private Button mStart,mHistory,mDescription;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_layout);
        mStart= (Button) findViewById(R.id.id_start_btn);
        mHistory= (Button) findViewById(R.id.id_hitory_btn);
        mDescription= (Button) findViewById(R.id.id_desciption_btn);
        mStart.setOnClickListener(this);
        mHistory.setOnClickListener(this);
        mDescription.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent=null;
        switch (v.getId()){
            case R.id.id_start_btn:
                intent=new Intent(WelComeActivity.this,MainActivity.class);
                break;
            case R.id.id_desciption_btn:
                intent=new Intent(WelComeActivity.this,DescriptionActivity.class);
                break;
            case R.id.id_hitory_btn:
                intent=new Intent(WelComeActivity.this,HistoryActivity.class);
                break;
        }
        startActivity(intent);
    }
}
