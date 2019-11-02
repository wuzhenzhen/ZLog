package com.github.zftpserver.server;

import androidx.annotation.NonNull;

import com.github.zftpserver.FsSettings;

import java.io.File;

public class FtpUser {

    final private String mUsername;
    final private String mPassword;
    final private String mChroot;

    public FtpUser(@NonNull String username, @NonNull String password, @NonNull String chroot) {
        mUsername = username;
        mPassword = password;

        final File rootPath = new File(chroot);
        mChroot = rootPath.isDirectory() ? chroot : FsSettings.getDefaultChrootDir().getPath();
    }

    public String getUsername() {
        return mUsername;
    }

    public String getPassword() {
        return mPassword;
    }

    public String getChroot() {
        return mChroot;
    }
}
