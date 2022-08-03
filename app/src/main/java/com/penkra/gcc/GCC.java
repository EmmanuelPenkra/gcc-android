package com.penkra.gcc;

import android.app.Application;
import android.content.Context;

public class GCC extends Application {

    public static Context context;

    public void onCreate() {
        super.onCreate();
        GCC.context = getApplicationContext();
    }

    public static Context getAppContext(){
        return GCC.context;
    }
}
