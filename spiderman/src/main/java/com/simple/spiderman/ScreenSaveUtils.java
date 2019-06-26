package com.simple.spiderman;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;

/**
 * @author xiemeng
 * @des
 * @date 2019-6-19 10:40
 */
public class ScreenSaveUtils {
    public boolean setDecorViewImage(Activity activity) {
//        try {
            //整个手机屏幕的视图
            View view = activity.getWindow().getDecorView();
            view.setDrawingCacheEnabled(true);
            view.buildDrawingCache();

            Bitmap bitmap = view.getDrawingCache();

            // 获取状态栏高度
            Rect frame = new Rect();
            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            int statusBarHeight = frame.top;
            Log.i("TAG", "" + statusBarHeight);

            // 获取屏幕长和高
            int width = activity.getWindowManager().getDefaultDisplay().getWidth();
            int height = activity.getWindowManager().getDefaultDisplay().getHeight();

            Bitmap b = Bitmap.createBitmap(bitmap, 0, statusBarHeight, width, height - statusBarHeight);
            savePic(b, activity);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }

        return true;
    }

    //    public
    // 保存到sdcard
    private static void savePic(Bitmap b, Activity activity) {
        FileOutputStream fos = null;
        final MediaScannerConnection[] mScanner = {null};
        try {
            File appDir = new File(Environment.getExternalStorageDirectory(), "广告标识小觅");
            if (!appDir.exists()) {
                appDir.mkdir();
            }

            String strRand = "";
            for (int i = 0; i < 15; i++) {
                strRand += String.valueOf((int) (Math.random() * 10));
            }
            Calendar calendar = Calendar.getInstance();
            //获取系统的日期
            //年
            int year = calendar.get(Calendar.YEAR);
            //月
            int month = calendar.get(Calendar.MONTH) + 1;
            //日
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            String fileName = year+"-"+month+"-"+day+"-"+strRand+".jpg";
//            String fileName = strRand + ".jpg";
            final File file = new File(appDir, fileName);
            fos = new FileOutputStream(file);
            if (null != fos) {
                b.compress(Bitmap.CompressFormat.PNG, 80, fos);
                fos.flush();
                fos.close();
            }
            MediaStore.Images.Media.insertImage(activity.getContentResolver(), file.getAbsolutePath(), fileName, null);
            // 最后通知图库更新
            activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(file.getAbsolutePath())));

            MediaScannerConnection.scanFile(activity,
                    new String[]{file.getAbsolutePath()},
                    new String[]{"image/jpeg"},
                    new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("pgpluginMain", "onScanCompleted" + path);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public boolean setPermissionsArray(Activity activity, String[] permission) {
        for (int i = 0; i < permission.length; i++) {
            if (ContextCompat.checkSelfPermission(activity, permission[i]) != PackageManager.PERMISSION_GRANTED) {
                Log.e("权限提醒====", "没有：" + permission[i] + "----的权限-----");
                ActivityCompat.requestPermissions(activity, new String[]{permission[i]}, 1);
                return false;
            } else {
                Log.e("权限提醒====", "有：" + permission[i] + "----的权限-----");
                return true;
            }
        }
        return true;
    }
}