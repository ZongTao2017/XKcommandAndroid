package com.xkglow.xkcommand;

import android.app.Application;

import com.xkglow.xkcommand.Helper.AppGlobal;

public class XKcommandApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppGlobal.init(this);
    }
}
