package com.ifading.namemachine.application;

import android.app.Application;

import com.ifading.namemachine.db.MyObjectBox;

import io.objectbox.BoxStore;
/**
 * Application
 *
 * Created by yangjingsheng on 17/10/14.
 */

public class BaseApplication extends Application {

    public static BoxStore getBoxStore() {
        return boxStore;
    }

    private static BoxStore boxStore;

    @Override
    public void onCreate() {
        super.onCreate();
        boxStore = MyObjectBox.builder().androidContext(BaseApplication.this).build();
    }
}
