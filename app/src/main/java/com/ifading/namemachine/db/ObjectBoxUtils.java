package com.ifading.namemachine.db;

import android.app.Application;

import com.ifading.namemachine.application.BaseApplication;

import io.objectbox.BoxStore;

/**
 * Created by yangjingsheng on 17/10/20.
 */

public class ObjectBoxUtils {
    public static BoxStore getBoxStore() {
        return boxStore;
    }

    private static BoxStore boxStore;

    public static void initDb(Application application){
        boxStore = MyObjectBox.builder().androidContext(application).build();
    }
}
