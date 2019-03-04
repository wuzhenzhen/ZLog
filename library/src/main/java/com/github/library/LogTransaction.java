package com.github.library;

import android.support.annotation.NonNull;

/**
 * Created by wzz on 2019/03/02.
 * kgd.zhen
 */
public abstract class LogTransaction {
    public abstract LogTransaction msg(@NonNull final Object msg);

    public abstract LogTransaction msgs(@NonNull final Object... msg);

    public abstract LogTransaction tag(@NonNull final String tag);

    public abstract LogTransaction tags(@NonNull final String... tags);

    public abstract LogTransaction file();

    public abstract LogTransaction file(@NonNull final String fileName);

    public abstract LogTransaction ln();

    public abstract LogTransaction format(@NonNull final String format, Object... args);

    public abstract LogTransaction out();
}
