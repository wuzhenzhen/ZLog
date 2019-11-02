package com.github.zftpserver;

import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;

import com.github.zftpserver.utils.Cat;

/**
 * Created by wzz on 2019/10/17.
 * kgd.zhen@gmail.com
 */
public class App{

    private static Context mContext;

    public static void initContext(Context context){
        mContext = context;

//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(FsService.ACTION_STARTED);
//        intentFilter.addAction(FsService.ACTION_STOPPED);
//        intentFilter.addAction(FsService.ACTION_FAILEDTOSTART);

//        registerReceiver(new AutoConnect.ServerActionsReceiver(), intentFilter);
//        context.registerReceiver(new NsdService.ServerActionsReceiver(), intentFilter);
//        registerReceiver(new FsWidgetProvider(), intentFilter);

//        AutoConnect.maybeStartService(this)
    }


    /**
     * @return the Context of this application
     */
    public static Context getAppContext() {
//        return FsService.mContext;
        return mContext;
    }

    /**
     * Get the version from the manifest.
     *
     * @return The version as a String.
     */
    public static String getVersion() {
        Context context = getAppContext();
        String packageName = context.getPackageName();
        try {
            PackageManager pm = context.getPackageManager();
            return pm.getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Cat.e("Unable to find the name " + packageName + " in the package");
            return null;
        }
    }
}
