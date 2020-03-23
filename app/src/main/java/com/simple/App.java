package com.simple;

import android.app.Application;
import android.content.Intent;
import android.widget.Toast;

import com.simple.spiderman.CrashActivity;
import com.simple.spiderman.CrashModel;
import com.simple.spiderman.SpiderMan;

/**
 * author : ChenPeng
 * date : 2018/4/21
 * description :
 */
public class App extends Application {


    private App mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext=this;
        //放在其他库初始化前
        SpiderMan.init(this, "11", new SpiderMan.CrashListener() {
            @Override
            public void crash(CrashModel model) {
                Toast.makeText(getApplicationContext(), "111", Toast.LENGTH_SHORT).show();
//                android.os.Process.killProcess(android.os.Process.myPid());
                SpiderMan.afterCrashListener(model);
            }
        });

    }

}
