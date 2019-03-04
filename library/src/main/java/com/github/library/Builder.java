package com.github.library;

import android.support.annotation.NonNull;

import java.io.File;

/**
 * Created by wzz on 2019/03/02.
 * kgd.zhen
 */
public class Builder {
    public String logSavePath = "";     //��־����·��
    public Character logCatLogLevel;    //����̨�����־�ȼ�
    public Character fileLogLevel;      //������־�ȼ�
    public Character fileOutFormat;     //������־��ʽ


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
}
