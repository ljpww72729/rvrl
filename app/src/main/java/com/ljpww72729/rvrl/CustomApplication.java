package com.ljpww72729.rvrl;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by LinkedME06 on 24/05/2017.
 */

public class CustomApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
