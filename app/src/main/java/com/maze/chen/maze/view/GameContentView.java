package com.maze.chen.maze.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;


import com.maze.chen.maze.GlobeContext;
import com.maze.chen.maze.MainActivity;
import com.maze.chen.maze.R;
import com.maze.chen.maze.controller.EffectButtonAnimator;

import java.util.concurrent.BlockingQueue;

/**
 * Created by chen on 2016/3/7.
 */
public class GameContentView extends RelativeLayout implements View.OnClickListener{
    private float mLastX,mLastY;
    /**
     * 用于接收方向信息的消息队列
     */
    private BlockingQueue<Integer> messageQueen;

    private int[] mSurTime;

    private Button mSubButton;
    private Button mBackButton;
    private Button mDestroyButton;
    private MainActivity mainActivity;

    private SurpriseButton surpriseButton;

    public GameContentView(Context context) {
        this(context,null);
    }

    public GameContentView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }


    public GameContentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mSurTime=new int[3];
        setFocusable(true);
        setClickable(true);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        for(int i=0;i<4;i++){
            View v=getChildAt(i);
            v.setOnClickListener(this);
            switch (v.getId()){
                case R.id.id_backward_button:
                    mBackButton= (Button)v;
                    break;
                case R.id.id_splitwall_button:
                    mDestroyButton= (Button)v;
                    break;
                case R.id.id_subv_button:
                    mSubButton= (Button)v;
                    break;
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec,heightMeasureSpec);
        setMeasuredDimension(GlobeContext.mScreenWidth,GlobeContext.mScreenHeight);
    }



    @Override
    public void onClick(View v) {
        Button cur=(Button)v;
        switch (cur.getId()){
            case R.id.id_open_button:
                float value=cur.getHeight()+2;
                if(cur.getText().equals("展开")){
                    new EffectButtonAnimator(mSubButton,-1,-value,true);
                    new EffectButtonAnimator(mBackButton,value,-value,true);
                    new EffectButtonAnimator(mDestroyButton, value,-1,true);
                    cur.setText("收起");
                }
                else{
                    cur.setText("展开");
                    new EffectButtonAnimator(mSubButton,0,0,false);
                    new EffectButtonAnimator(mBackButton,0,0,false);
                    new EffectButtonAnimator(mDestroyButton, 0,0,false);
                }
                break;
            case R.id.id_backward_button:
                mainActivity.onEffectClick(2);
                break;
            case R.id.id_splitwall_button:
                mainActivity.onEffectClick(1);
                break;
            case R.id.id_subv_button:
                mainActivity.onEffectClick(0);
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mLastX=event.getX();
                mLastY=event.getY();
                break;
            case MotionEvent.ACTION_UP:
                confirmDirection(event);
        }
        return super.onTouchEvent(event);
    }

    /**
     * 根据手点手起来确定方向
     * @param event
     */
    private void confirmDirection(MotionEvent event){
        try {
            float curX = event.getX();
            float curY = event.getY();
            //确定方向，上下之差大于左右则为上下
            if (Math.abs(curY - mLastY) - Math.abs(curX - mLastX) > 0) {
                if (curY < mLastY) {
                    messageQueen.put(0);
                } else {
                    messageQueen.put(1);
                }
            } else {
                if(curX<mLastX){
                    messageQueen.put(2);
                }
                else{
                    messageQueen.put(3);
                }
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setMessageQueen(BlockingQueue<Integer> messageQueen) {
        this.messageQueen = messageQueen;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public void checkIfShouldAddSurprise(){
        for(int i=2;i>=0;i--){
            if(--mSurTime[i]==0){
                SurpriseButton btn=new SurpriseButton(getContext());
                btn.initEvent(this);
                surpriseButton=btn;
            }
        }

    }

    public void startGetAnimator(boolean flag){
        surpriseButton.startGetAnimator(flag);
    }

    /**
     * 用于设置答题按钮出现的时间
     */
    public void initSurTime(){
        mSurTime[0]= (int) (Math.random()*8+7);
        for(int i=1;i<3;i++){
            int interval= (int) ((Math.random()+0.3)*(8*i));
            mSurTime[i]= (int) (mSurTime[i-1]+Math.random()*10+interval);
        }
    }

    /**
     * 设置文字
     * @param text
     * @param flag
     */

    public void setButtonText(String text,int flag){
        if(flag==0){
            mSubButton.setText(text);
        }
        else if(flag==1){
            mBackButton.setText(text);
        }
        else{
            mDestroyButton.setText(text);
        }
    }
    public void stopCheckSurprise(){
        for(int i=0;i<3;i++){
            mSurTime[i]=-1;
        }
    }
}
