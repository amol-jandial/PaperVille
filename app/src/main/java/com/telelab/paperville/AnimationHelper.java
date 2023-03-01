package com.telelab.paperville;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class AnimationHelper {
    public static void textScaleAnimation(TextView textView, float firstSize, float endSize){
        ValueAnimator animator = ValueAnimator.ofFloat(firstSize, endSize);
        animator.setDuration(600);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator valueAnimator) {
                float animatedValue = (float) valueAnimator.getAnimatedValue();
                textView.setTextSize(animatedValue);
            }
        });
        animator.start();

    }

}
