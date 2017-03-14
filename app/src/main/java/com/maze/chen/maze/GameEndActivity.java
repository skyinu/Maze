package com.maze.chen.maze;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by chen on 2016/3/10.
 */
public class GameEndActivity extends BaseActivity implements View.OnClickListener {
    private boolean flag;
    private Button mNextButton;
    private Button mShareButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_end_layout);
        if(getIntent()==null){
            finish();
        }
        flag = getIntent().getBooleanExtra("game_result", false);
        mNextButton = (Button) findViewById(R.id.id_next_pass);
        mShareButton = (Button) findViewById(R.id.id_share);
        mShareButton.setOnClickListener(this);
        mNextButton.setOnClickListener(this);
        ImageView imageView = (ImageView) findViewById(R.id.id_end_image);
        if (!flag) {
            imageView.setImageResource(R.drawable.game_failure);
            setTitle("植物逃亡失败");
            setTitleColor(Color.GRAY);
            mNextButton.setText(getResources().getString(R.string.str_pass_again));
            mNextButton.setBackground(getResources().getDrawable(R.drawable.gray_button));
        } else {
            setTitleColor(Color.RED);
            imageView.setImageResource(R.drawable.game_successful);
            setTitle("植物逃亡成功");
            mNextButton.setBackground(getResources().getDrawable(R.drawable.blue_button));
        }
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_next_pass:
                if (flag) {
                    setResult(RESULT_OK);
                } else {
                    setResult(RESULT_CANCELED);
                }
                break;
            case R.id.id_share:
                Intent intent=new Intent();
                setResult(RESULT_OK,intent);
                break;
        }
        finish();
    }
}
