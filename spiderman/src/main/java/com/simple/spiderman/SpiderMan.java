package com.simple.spiderman;

import android.content.Context;
import android.content.Intent;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

public class SpiderMan implements Thread.UncaughtExceptionHandler {

    public static final String TAG = "SpiderMan";

    private static SpiderMan spiderMan = new SpiderMan();

    private static Context mContext;

    private static String mVersionCode;
    private Thread.UncaughtExceptionHandler mExceptionHandler;
//    private OnCrashListener mOnCrashListener;

    private SpiderMan() {
        mExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }


    public interface CrashListener {
        void crash(CrashModel model);
    }


    private static CrashListener mListener;

    public static SpiderMan init(Context context) {
        mContext = context;
        return spiderMan;
    }

    public static SpiderMan init(Context context, String versionCode) {
        mContext = context;
        mVersionCode = versionCode;
        return spiderMan;
    }

    /**
     *  如果还使用跳转的activity，监听事件需要自行调用afterCrashListener方法
     *  不启用需要自行关闭进程  android.os.Process.killProcess(android.os.Process.myPid());
     * @param context
     * @param versionCode
     * @param listener
     * @return
     */
    public static SpiderMan init(Context context, String versionCode,CrashListener listener) {
        mContext = context;
        mVersionCode = versionCode;
        mListener = listener;
        return spiderMan;
    }

    @Override
    public void uncaughtException(Thread t, Throwable ex) {
        CrashModel model = parseCrash(ex);
        handleException(model);
    }

    private void handleException(CrashModel model) {
        if (null != mListener) {
            mListener.crash(model);
        } else {
            afterCrashListener(model);
        }
    }

    /**
     * 如果停止服务后监听了事件还需要启动报错弹窗，使用该方法
     * @param model
     */
    public static void afterCrashListener(CrashModel model) {
        Intent intent = new Intent(mContext, CrashActivity.class);
        intent.putExtra(CrashActivity.CRASH_MODEL, model);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    private CrashModel parseCrash(Throwable ex) {
        CrashModel model = new CrashModel();
        try {
            model.setEx(ex);
            model.setTime(new Date().getTime());
            if (ex.getCause() != null) {
                ex = ex.getCause();
            }
            model.setVersionCode(mVersionCode);
            model.setExceptionMsg(ex.getMessage());
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            pw.flush();
            String exceptionType = ex.getClass().getName();

            if (ex.getStackTrace() != null && ex.getStackTrace().length > 0) {
                StackTraceElement element = ex.getStackTrace()[0];

                model.setLineNumber(element.getLineNumber());
                model.setClassName(element.getClassName());
                model.setFileName(element.getFileName());
                model.setMethodName(element.getMethodName());
                model.setExceptionType(exceptionType);
            }

            model.setFullException(sw.toString());
        } catch (Exception e) {
            return model;
        }
        return model;
    }

//    public interface OnCrashListener {
//        void onCrash(Thread t, Throwable ex, CrashModel model);
//    }
//
//    public void setOnCrashListener(OnCrashListener listener) {
//        this.mOnCrashListener = listener;
//    }


}