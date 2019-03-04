package com.github.library;

/**
 * Created by wzz on 2019/03/02.
 * kgd.zhen
 */
public enum LogLevel {
    Verbose(ZLog.SHOW_VERBOSE_LOG),
    Debug(ZLog.SHOW_DEBUG_LOG),
    Info(ZLog.SHOW_INFO_LOG),
    Warn(ZLog.SHOW_WARN_LOG),
    Error(ZLog.SHOW_ERROR_LOG),
    Json(ZLog.SHOW_JSON_LOG),;

    LogLevel(int value) {
        this.value = value;
    }

    final int value;
}
