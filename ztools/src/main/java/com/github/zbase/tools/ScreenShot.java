package com.github.zbase.tools;

import java.io.File;  
import java.io.FileNotFoundException;  
import java.io.FileOutputStream;  
import java.io.IOException;
import java.lang.reflect.Method;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.github.library.ZLog;

public class ScreenShot {  
  
    private static Bitmap takeScreenShot(Activity activity) {  
        // View是你需要截图的View  
        View view = activity.getWindow().getDecorView();  
        view.setDrawingCacheEnabled(true);  
        view.buildDrawingCache();  
        Bitmap b1 = view.getDrawingCache();  
  
        // 获取状态栏高度  
        Rect frame = new Rect();  
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);  
        int statusBarHeight = frame.top;  
  
        // 获取屏幕长和高  
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();  
        int height = activity.getWindowManager().getDefaultDisplay()  
                .getHeight();  
        // 去掉标题栏  
        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height  
                - statusBarHeight);  
        view.destroyDrawingCache();  
        return b;  
    }  
  
    private static void savePic(Bitmap b, File filePath) {  
        FileOutputStream fos = null;  
        try {  
            fos = new FileOutputStream(filePath);  
            if (null != fos) {  
                b.compress(Bitmap.CompressFormat.PNG, 100, fos);  
                fos.flush();
            }
        } catch (FileNotFoundException e) {
             e.printStackTrace();
        } catch (IOException e) {  
             e.printStackTrace();
        } finally{
            try{
                if(fos != null){
                    fos.close();
                }
                if(b != null){
                    b.recycle();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }  
  
    public static void shoot(Activity a, File filePath) {  
        if (filePath == null) {  
            return;  
        }  
        if (!filePath.getParentFile().exists()) {  
            filePath.getParentFile().mkdirs();  
        }
        ZLog.iii("--ScreenShot--shoot--filePath--"+filePath.getAbsolutePath());
        ScreenShot.savePic(ScreenShot.takeScreenShot(a), filePath);  
    }  
    
    public static void shoot(Activity a,String path){
    	File imgFile = new File(path);
    	shoot(a,imgFile);
    }

    /**
     *   screencap -p 命令来截屏 （注 电子站牌Android板不支持此方法）
     */
    public static void takeScreenShot(String path){
        File imgFile = new File(path);
        if (!imgFile.getParentFile().exists()) {
            imgFile.getParentFile().mkdir();
        }
        ZLog.iii("--takeScreenShot--screencap--"+path);
        ShellUtils.CommandResult result = ShellUtils.execCommand("screencap -p " + path, true);
        ZLog.iii("--takeScreenShot--screencap=="+result.toString());
    }

    /**
     *  模拟按键截屏
     *  adb shell input keyevent 120  || adb shell input keyevent KEYCODE_SYSRQ
     */
    public static void sendScreenShotCmd(){
        ShellUtils.CommandResult result = ShellUtils.execCommand("input keyevent KEYCODE_SYSRQ", true); //默认截取到Picture里
        ZLog.iii("--SendScreenShotCmd--=="+result.toString());
    }

    public static boolean takeScreenShot2(Context context, String path) {
        View dView = ((Activity) context).getWindow().getDecorView();
        dView.setDrawingCacheEnabled(true);
        dView.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(dView.getDrawingCache());
        if (bitmap != null) {
            try {
                // 图片文件路径
                File file = new File(path);
                FileOutputStream os = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                os.flush();
                os.close();
            } catch (Exception e) {
            }
        }
        return true;
    }

    /**
     *  截屏方法
     *  Android4.0 -- Android4.2  android.view.Surface.screenshot()  隐藏方法，需要通过反射调用
     *  Android4.3 之后 screenshot 移到android.view.SurfaceControl，SurfaceControl是隐藏类，需要通过反射调用
     *  注：此方法必须要系统签名才可以!
     * @param context
     * @param imagePath  保存路径 如：sdcard/123.png
     * @return true 截屏成功，false截屏失败
     */
    public static boolean takeScreenShot3(Context context, String imagePath){
        if(TextUtils.isEmpty(imagePath)){
            return false;
        }

        WindowManager mWindowManager;
        DisplayMetrics mDisplayMetrics;
        Display mDisplay;

        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mDisplay = mWindowManager.getDefaultDisplay();
        mDisplayMetrics = new DisplayMetrics();
        mDisplay.getRealMetrics(mDisplayMetrics);

        float[] dims = {mDisplayMetrics.widthPixels , mDisplayMetrics.heightPixels };
        Class testClass;
        try {
            File imageFile = new File(imagePath);
            if (!imageFile.getParentFile().exists()) {
                boolean result = imageFile.getParentFile().mkdirs();
                ZLog.iii("takeScreenShot3--result--"+result);
            }

            testClass = Class.forName("android.view.SurfaceControl");
            Method saddMethod1 = testClass.getMethod("screenshot", new Class[]{int.class ,int.class});
            Bitmap screenBitmap = (Bitmap) saddMethod1.invoke(null, new Object[]{(int) dims[0], (int) dims[1]});
            if (screenBitmap == null) {
                ZLog.eee("----takeScreenShot3---screenBitmap is null--");
                return false ;
            }
            FileOutputStream out = new FileOutputStream(imagePath);
            screenBitmap.compress(Bitmap.CompressFormat. PNG, 100, out);
        } catch (Exception e){
            ZLog.eee("----takeScreenShot3---e="+e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true ;
    }
}