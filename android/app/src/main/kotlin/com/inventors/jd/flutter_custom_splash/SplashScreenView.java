package com.inventors.jd.flutter_custom_splash;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.ViewPropertyAnimator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

class SplashScreenView extends RelativeLayout {
    //rotate animation variables
    private static final int ANIMATION_TIME_IN_MILLIS = 800;
    //fade animation variables
    private static final int TRANSITION_TIME_IN_MILLIS = 1000;
    final int ID_ONE = 1;
    private final ImageView flutterLogo;
    private float rotateAngle = 360;
    private ViewPropertyAnimator rotateAnimator;
    // Listener for rotateAnimator for event onAnimationEnd and onAnimationCancel
    private final Animator.AnimatorListener rotateAnimatorListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            animation.removeAllListeners();
            rotateAngle = rotateAngle + 360;
            animateFlutterLogo();
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            animation.removeAllListeners();
        }
    };
    private float transitionPercentWhenAnimationStarted = 0.0f;
    private float totalTransitionPercent = 0.0f;
    //update Listener for transition animator
    private final ValueAnimator.AnimatorUpdateListener transitionAnimatorUpdateListener = animation -> totalTransitionPercent = transitionPercentWhenAnimationStarted +
            (animation.getAnimatedFraction() * (1.0f - transitionPercentWhenAnimationStarted));
    private Runnable onTransitionComplete;
    private final Animator.AnimatorListener transitionAnimatorListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            animation.removeAllListeners();
            if (onTransitionComplete != null) {
                onTransitionComplete.run();
            }
        }
    };

    //main function
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SplashScreenView(Context context) {
        super(context);

        setBackgroundColor(Color.parseColor("#F3D34A"));

        flutterLogo = new ImageView(getContext());

        TextView txtVersion = new TextView(getContext());
        txtVersion.setText("by JD");
        txtVersion.setTextSize(12.0f);
        txtVersion.setId(ID_ONE);

        flutterLogo.setImageDrawable(getResources().getDrawable(R.mipmap.ic_launcher, getContext().getTheme()));

        RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        layoutParams1.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

//        addView(flutterLogo, new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER));

        addView(txtVersion, layoutParams1);


        TextView txtName = new TextView(getContext());
        txtName.setText("Splash Screen");
        txtName.setTextSize(35.0f);
        txtName.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        layoutParams2.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        layoutParams2.addRule(RelativeLayout.ABOVE, ID_ONE);
        addView(txtName, layoutParams2);

//        animateFlutterLogo();
    }

    //rotation animation
    private void animateFlutterLogo() {
        rotateAnimator = flutterLogo
                .animate().rotation(rotateAngle).setDuration(ANIMATION_TIME_IN_MILLIS)
                .setInterpolator(new LinearInterpolator())
                .setListener(rotateAnimatorListener);
        rotateAnimator.start();
    }

    //transition animation
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void animateAway(@NonNull Runnable onTransitionComplete) {
        this.onTransitionComplete = onTransitionComplete;
        ViewPropertyAnimator fadeAnimator = animate()
                .alpha(0.0f)
                .setDuration(Math.round(TRANSITION_TIME_IN_MILLIS * (1.0 - totalTransitionPercent)))
                .setUpdateListener(transitionAnimatorUpdateListener)
                .setListener(transitionAnimatorListener);
        fadeAnimator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        if (rotateAnimator != null) {
            rotateAnimator.cancel();
        }
        super.onDetachedFromWindow();
    }

    //state saving
    @Nullable
    public Bundle saveSplashState() {
        Bundle state = new Bundle();
        state.putFloat("totalTransitionPercent", totalTransitionPercent);
        return state;
    }

    public void restoreSplashState(@Nullable Bundle bundle) {
        if (bundle != null) {
            transitionPercentWhenAnimationStarted = bundle.getFloat("totalTransitionPercent");
            setAlpha(1.0f - transitionPercentWhenAnimationStarted);
        }
    }
}