package com.ifading.namemachine.application;

import android.app.Application;

import com.ifading.namemachine.db.ObjectBoxUtils;
/**
 * Application
 *
 * Created by yangjingsheng on 17/10/14.
 */

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ObjectBoxUtils.initDb(this);

    }
}
