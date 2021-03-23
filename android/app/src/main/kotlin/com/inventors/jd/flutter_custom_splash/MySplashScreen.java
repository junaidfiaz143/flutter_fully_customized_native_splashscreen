package com.inventors.jd.flutter_custom_splash;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import io.flutter.embedding.android.SplashScreen;

class MySplashScreen implements SplashScreen {

   SplashScreenView mySplashScreenView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View createSplashView(@NonNull Context context, @Nullable Bundle savedInstanceState) {
        if (mySplashScreenView == null) {
            mySplashScreenView = new SplashScreenView(context);
            mySplashScreenView.restoreSplashState(savedInstanceState);
        }
        return mySplashScreenView;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void transitionToFlutter(@NonNull Runnable onTransitionComplete) {
        if (mySplashScreenView != null) {
            mySplashScreenView.animateAway(onTransitionComplete);
        } else {
            onTransitionComplete.run();
        }
    }

    @Override
    public boolean doesSplashViewRememberItsTransition() {
        return true;
    }

    @Nullable
    @Override
    public Bundle saveSplashScreenState() {
        if (mySplashScreenView != null) {
            return mySplashScreenView.saveSplashState();
        } else {
            return null;
        }
    }
}
