package com.telelab.paperville;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.google.android.material.textfield.TextInputLayout;
import com.telelab.paperville.models.DataWrapper;
import com.telelab.paperville.models.Subject;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class HelperMethods {
    private static final String TAG = "BaseActivity";

    public static void startNewActivity(Activity activity, Class clas){
        Intent intent = new Intent(activity, clas);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }

    public static void startNewActivityWithData(Activity activity, Class clas, ArrayList<Subject> subjects){
        Intent intent = new Intent(activity, clas);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("subjects", new DataWrapper(subjects));
        activity.startActivity(intent);
    }

    public static void pushNewActivity(Activity activity, Class clas){
        Intent intent = new Intent(activity, clas);
        activity.startActivity(intent);
    }

    public static void pushNewRoutingActivity(Activity activity, Class clas){
        Intent intent = new Intent(activity, clas);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
    }


    public static void onKeyboardDownListener(View rootView, TextInputLayout inputLayout){
        final boolean[] keyboardShown = new boolean[1];
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                rootView.getWindowVisibleDisplayFrame(r);
                int heightDiff = rootView.getRootView().getHeight() - r.height();
                if(heightDiff > 0.25*rootView.getRootView().getHeight()){
                    keyboardShown[0] = true;
                }else{
                    if(keyboardShown[0]){
                        keyboardShown[0] = false;
                        inputLayout.clearFocus();
                    }
                    }
                }
            });
    }

    public static void onKeyboardDownListener(View rootView, EditText editText){
        final boolean[] keyboardShown = new boolean[1];
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                rootView.getWindowVisibleDisplayFrame(r);
                int heightDiff = rootView.getRootView().getHeight() - r.height();
                if(heightDiff > 0.25*rootView.getRootView().getHeight()){
                    keyboardShown[0] = true;
                }else{
                    if(keyboardShown[0]){
                        keyboardShown[0] = false;
                        editText.clearFocus();
                    }
                }
            }
        });
    }

    public static void enableButton(AppCompatButton button, AppCompatButton continueButton, Context context){
        if(button != null){
            button.setClickable(true);
            button.setBackgroundDrawable(HelperMethods.getDrawable(R.drawable.button_next_done, context));
        }
        continueButton.setText("Continue");
        continueButton.setClickable(true);
        continueButton.setBackgroundDrawable(HelperMethods.getDrawable(R.drawable.button_registration_done,
                context));
    }

    public static void disableButton(AppCompatButton button, AppCompatButton continueButton, String text,
                                     Context context){
        button.setClickable(false);
        button.setBackgroundDrawable(HelperMethods.getDrawable(R.drawable.button_next_pending, context));
        continueButton.setText(text);
        continueButton.setClickable(false);
        continueButton.setBackgroundDrawable(HelperMethods.getDrawable(R.drawable.button_registration,
                context));
    }

    public static Drawable getDrawable(int id, Context context){
        return context.getResources().getDrawable(id);
    }


    public static void nextText(int firstID, int nextID, Context context, View rootView){
        TextView first = rootView.findViewById(firstID);
        Resources r = context.getResources();
        first.setTextColor(context.getResources().getColor(R.color.highlight));
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) first.getLayoutParams();
        params.setMarginStart(Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 29,
                r.getDisplayMetrics())));
        first.setLayoutParams(params);
        AnimationHelper.textScaleAnimation(first, 18, 16);
        if(nextID != 0){
            TextView next = rootView.findViewById(nextID);
            next.setTextColor(context.getResources().getColor(R.color.text_highlight));
            next.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            params = (ConstraintLayout.LayoutParams) next.getLayoutParams();
            params.setMarginStart(Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24,
                    r.getDisplayMetrics())));
            next.setLayoutParams(params);
            AnimationHelper.textScaleAnimation(next, 16, 18);
        }

    }

    public static void prevText(int currentID, int previousID, Context context, View rootView){
        TextView current = rootView.findViewById(currentID);
        TextView previous = rootView.findViewById(previousID);
        Resources r = context.getResources();
        previous.setTextColor(context.getResources().getColor(R.color.non_highlight));
        current.setTextColor(context.getResources().getColor(R.color.text_highlight));
        previous.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        current.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) previous.getLayoutParams();
        params.setMarginStart(Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 29,
                r.getDisplayMetrics())));
        previous.setLayoutParams(params);
        params = (ConstraintLayout.LayoutParams) current.getLayoutParams();
        params.setMarginStart(Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24,
                r.getDisplayMetrics())));
        current.setLayoutParams(params);
        AnimationHelper.textScaleAnimation(current, 16, 18);
        AnimationHelper.textScaleAnimation(previous, 18, 16);

    }

    public static void nextTimeline(int firstCircleID, int secondCircleID, int lineBetweenID,
                                    Context context, View rootView){
        View firstCircle = rootView.findViewById(firstCircleID);
        View lineBetween = rootView.findViewById(lineBetweenID);
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) firstCircle.getLayoutParams();
        Resources r = context.getResources();
        params.height = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, r.getDisplayMetrics()));
        params.width = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, r.getDisplayMetrics()));
        firstCircle.setLayoutParams(params);
        firstCircle.setBackground(context.getResources().getDrawable(R.drawable.circle_filled_tick));


        if(secondCircleID != 0){
            View secondCircle = rootView.findViewById(secondCircleID);
            params = (ConstraintLayout.LayoutParams) secondCircle.getLayoutParams();
            params.height = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, r.getDisplayMetrics()));
            params.width = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, r.getDisplayMetrics()));
            secondCircle.setLayoutParams(params);
            secondCircle.setBackground(context.getResources().getDrawable(R.drawable.circle_current));
        }

        lineBetween.setBackgroundColor(context.getResources().getColor(R.color.highlight));
    }

    public static void prevTimeline(int currentCircleID, int previousCircleID, int lineBetweenID,
                                    Context context, View rootView){
        View currentCircle = rootView.findViewById(currentCircleID) ;
        View previousCircle = rootView.findViewById(previousCircleID);
        View lineBetween = rootView.findViewById(lineBetweenID);
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) currentCircle.getLayoutParams();
        Resources r = context.getResources();
        params.height = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, r.getDisplayMetrics()));
        params.width = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, r.getDisplayMetrics()));
        currentCircle.setLayoutParams(params);
        currentCircle.setBackground(context.getResources().getDrawable(R.drawable.circle_current));
        params = (ConstraintLayout.LayoutParams) previousCircle.getLayoutParams();
        params.height = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, r.getDisplayMetrics()));
        params.width = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, r.getDisplayMetrics()));
        previousCircle.setLayoutParams(params);
        previousCircle.setBackground(context.getResources().getDrawable(R.drawable.circle_pending));

        lineBetween.setBackgroundColor(context.getResources().getColor(R.color.non_highlight));
    }

    public static void slideView(View view, int currentHeight, int newHeight){
        ValueAnimator slideAnimator = ValueAnimator.ofInt(currentHeight, newHeight).setDuration(500);

        slideAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator valueAnimator) {
                Integer value = (Integer) valueAnimator.getAnimatedValue();
                view.getLayoutParams().height = value;
                view.requestLayout();
            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.play(slideAnimator);
        animatorSet.start();
    }

    public static int pxToDp(int px, Resources res){
        return  Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px,
                res.getDisplayMetrics()));
    }

}
