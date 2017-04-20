package com.vbazh.translater_yandex;

import android.app.Application;

import com.letv.sarrsdesktop.blockcanaryex.jrt.BlockCanaryEx;
import com.letv.sarrsdesktop.blockcanaryex.jrt.Config;
import com.squareup.leakcanary.LeakCanary;


/**
 *
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

//        if(!BlockCanaryEx.isInSamplerProcess(this)) {
//            BlockCanaryEx.install(new Config(this));
//        }
//
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(this);

    }
}
