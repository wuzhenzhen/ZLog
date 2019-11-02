package com.github.zftpserver.utils;

import com.github.zftpserver.FsService;

/**
 * Created by wzz on 2019/10/17.
 * kgd.zhen@gmail.com
 */
public class Cat {
    public static void d(String content){
        log(content);
    }

    public static void v(String content){
        log(content);
    }

    public static void i(String content){
        log(content);
    }

    public static void w(String content){
        log(content);
    }

    public static void e(String content){
        log(content);
    }

    public static void d(String tag,String content){
        log(tag+"--"+content);
    }

    public static void v(String tag,String content){
        log(tag+"--"+content);
    }

    public static void i(String tag,String content){
        log(tag+"--"+content);
    }

    public static void w(String tag,String content){
        log(tag+"--"+content);
    }

    public static void e(String tag,String content){
        log(tag+"--"+content);
    }

    public static void log(String log){
        if(FsService.mFtpListener != null){
            FsService.mFtpListener.log(log);
        }else{
            System.out.println(log);
        }
    }
}
