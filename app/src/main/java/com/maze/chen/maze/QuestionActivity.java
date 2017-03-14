package com.maze.chen.maze;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by chen on 2015/12/10.
 */
public class QuestionActivity extends BaseActivity implements View.OnClickListener{
    private Button btnSure;
    private RadioGroup radioGroup;
    private TextView tv_question;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioButton radioButton3;
    private int index;
    private int answer;
    private static Random random=new Random(System.currentTimeMillis());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("请回答问题");
        setContentView(R.layout.question_layout);
        btnSure= (Button) findViewById(R.id.btn_sure);
        tv_question= (TextView) findViewById(R.id.question);
        radioGroup= (RadioGroup) findViewById(R.id.radio_container);
        radioButton1= (RadioButton) findViewById(R.id.radioButton);
        radioButton2= (RadioButton) findViewById(R.id.radioButton2);
        radioButton3= (RadioButton) findViewById(R.id.radioButton3);
        btnSure.setOnClickListener(this);
        InitData();
    }

    /**
     * 选择题目、初始化显示数据
     */
    private void InitData() {
        index= random.nextInt(30);

        int questionId=getResources().getIdentifier("question"+index,"string",getPackageName());
        String question=getResources().getString(questionId);
        tv_question.setText(question);

        int itemaId=getResources().getIdentifier("itema"+index,"string",getPackageName());
        String itema=getResources().getString(itemaId);
        radioButton1.setText(itema);

        int itembId=getResources().getIdentifier("itemb"+index,"string",getPackageName());
        String itemb=getResources().getString(itembId);
        radioButton2.setText(itemb);

        int itemcId=getResources().getIdentifier("itemc"+index,"string",getPackageName());
        String itemc=getResources().getString(itemcId);
        radioButton3.setText(itemc);

        int answerId=getResources().getIdentifier("answer"+index,"integer",getPackageName());
        answer=getResources().getInteger(answerId);
    }

    @Override
    public void onClick(View v) {
        int id=radioGroup.getCheckedRadioButtonId();
        int rescode=RESULT_OK;
        int rid=getRightAnswerId();
        RadioButton radio=(RadioButton)findViewById(rid);
        if(id!=rid)
        {
            radio.setTextColor(Color.RED);
            rescode=RESULT_CANCELED;
            Animation tip= AnimationUtils.loadAnimation(this,R.anim.vibrate_radio);
            radio.startAnimation(tip);
        }
        final int res=rescode;
        btnSure.postDelayed(new Runnable() {
            @Override
            public void run() {
                setResult(res, null);
                finish();
                overridePendingTransition(R.anim.window_out,R.anim.window_out);
            }
        },500);
    }

    /**
     * 获取正确答案的RadioButton的id
     * @return
     */
    private int getRightAnswerId() {
        if(answer==1) {
            return R.id.radioButton;
        }
        else if(answer==2){
            return R.id.radioButton2;
        }
        else{
            return R.id.radioButton3;
        }
    }
}

