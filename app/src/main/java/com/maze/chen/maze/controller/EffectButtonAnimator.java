package com.maze.chen.maze.controller;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.Button;

/**
 * Created by chen on 2016/3/9.
 */
public class EffectButtonAnimator implements Animator.AnimatorListener {
    private Button button;
    private boolean flag;
    public EffectButtonAnimator(Button button,float xValue,float yValue,boolean flag) {
        this.button = button;
        this.flag=flag;
        ObjectAnimator objectAnimatorX=null;
        ObjectAnimator objectAnimatorY=null;
        if(xValue!=-1) {
            objectAnimatorX = ObjectAnimator.ofFloat(button, "translationX", xValue);
        }
        if(yValue!=-1) {
            objectAnimatorY = ObjectAnimator.ofFloat(button, "translationY", yValue);
        }
        AnimatorSet set=new AnimatorSet();
        if(objectAnimatorX!=null&&objectAnimatorY!=null) {
            set.playTogether(objectAnimatorX, objectAnimatorY);
        }
        else if(objectAnimatorX!=null){
            set.playTogether(objectAnimatorX);
        }
        else{
            set.playTogether(objectAnimatorY);
        }
        set.addListener(this);
        set.start();
    }

    @Override
    public void onAnimationStart(Animator animation) {
        if(flag){
            button.setEnabled(true);
            button.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        if(!flag){
            button.setEnabled(false);
            button.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
