package com.github.zlog;

import android.app.Application;

import com.github.library.Builder;
import com.github.library.ZLog;

/**
 * Created by wzz on 2019/03/02.
 * kgd.zhen
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //初始化Logcat 配置更多信息
        Builder builder = ZLog.newBuilder();
        builder.logSavePath("/sdcard/EBSB_SDCardLog"); //设置Log 保存的文件夹
        builder.logCatLogLevel(ZLog.SHOW_ALL_LOG); //控制台输出日志等级
        builder.fileLogLevel(ZLog.SHOW_ALL_LOG);   //保存文件日志等级
        builder.fileOutFormat(ZLog.LOG_OUTPUT_FORMAT_3);
        ZLog.initialize(this, builder.build());

    }
}
