package com.maze.chen.maze.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.RelativeLayout;


import com.maze.chen.maze.GlobeContext;
import com.maze.chen.maze.R;

import java.util.Deque;
import java.util.LinkedList;

/**
 * Created by chen on 2016/3/9.
 */
public class SurpriseButton extends Button implements View.OnClickListener, Animator.AnimatorListener {

    private ObjectAnimator transAnimator;
    private ObjectAnimator trans;
    public SurpriseButton(Context context) {
        super(context);
        transAnimator = ObjectAnimator.ofFloat(this, "translationY", GlobeContext.mScreenHeight);
        transAnimator.setInterpolator(new AccelerateInterpolator());
        transAnimator.setDuration(3000);
        setBackgroundResource(R.drawable.surprise_sun);
        setOnClickListener(this);
    }

    public void initEvent(RelativeLayout container) {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setTranslationX((float) (Math.random() * (GlobeContext.mScreenWidth - 70)));
        container.addView(this, lp);
        transAnimator.start();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.setAction("com.maze.question");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        ((GameContentView) SurpriseButton.this.getParent()).getMainActivity().startActivityForResult(intent, 0);
        trans = ObjectAnimator.ofFloat(this, "translationX", 0+ TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,30,new DisplayMetrics()));
        trans.setDuration(3000 - transAnimator.getCurrentPlayTime());
        trans.addListener(this);
        transAnimator.cancel();
        setOnClickListener(null);

    }

    public void startGetAnimator(boolean flag){
        if(flag) {
            transAnimator = ObjectAnimator.ofFloat(this, "translationY", GlobeContext.mScreenHeight-TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,30,new DisplayMetrics()));
            transAnimator.setInterpolator(new AccelerateInterpolator());
            transAnimator.setDuration(trans.getDuration());
            transAnimator.start();
            trans.start();
        }
        else{
            ((RelativeLayout) SurpriseButton.this.getParent()).removeView(this);
        }
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        ((RelativeLayout) SurpriseButton.this.getParent()).removeView(this);

    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
