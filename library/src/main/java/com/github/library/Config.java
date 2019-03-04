package com.github.library;

/**
 * Created by wzz on 2019/03/02.
 * kgd.zhen
 */
public class Config {
    // 存放日志文件的目录全路径
    public String logSavePath = "";
    public Character logCatLogLevel;
    public Character fileLogLevel;
    public Character fileOutFormat;

    public Config(Builder builder) {
        this.logSavePath = builder.logSavePath;
        this.logCatLogLevel = builder.logCatLogLevel;
        this.fileLogLevel = builder.fileLogLevel;
        this.fileOutFormat = builder.fileOutFormat;
    }
}
