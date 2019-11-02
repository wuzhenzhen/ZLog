package com.github.library;

import androidx.annotation.NonNull;

import java.io.File;

/**
 * Created by wzz on 2019/03/02.
 * kgd.zhen
 */
public class Builder {
    public String logSavePath = "";     //日志保存路径
    public Character logCatLogLevel;    //控制台输出日志等级
    public Character fileLogLevel;      //保存日志等级
    public Character fileOutFormat;     //保存日志格式
    public boolean dbFlowStatistics; //流量统计


    public Config build() {
        return new Config(this);
    }

    public Builder logSavePath(@NonNull String logSavePath) {
        this.logSavePath = logSavePath;
        return this;
    }

    public Builder logSavePath(@NonNull File logSavePath) {
        this.logSavePath = logSavePath.getAbsolutePath();
        return this;
    }

    public Builder logCatLogLevel(Character logCatLogLevel) {
        this.logCatLogLevel = logCatLogLevel;
        return this;
    }

    public Builder fileLogLevel(Character fileLogLevel) {
        this.fileLogLevel = fileLogLevel;
        return this;
    }

    public Builder logCatLogLevel(int logCatLogLevel) {
        this.logCatLogLevel = (char) logCatLogLevel;
        return this;
    }

    public Builder fileLogLevel(int fileLogLevel) {
        this.fileLogLevel = (char) fileLogLevel;
        return this;
    }

    public Builder fileOutFormat(int fileOutFormat) {
        this.fileOutFormat = (char) fileOutFormat;
        return this;
    }

    public Builder dbFlowStatistics(boolean isEnable){
        this.dbFlowStatistics = isEnable;
        return this;
    }
}
