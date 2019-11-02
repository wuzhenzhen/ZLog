/*
Copyright 2011-2013 Pieter Pareit
Copyright 2009 David Revell

This file is part of SwiFTP.

SwiFTP is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

SwiFTP is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with SwiFTP.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.github.zftpserver;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.github.zftpserver.server.FtpUser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class FsSettings {

    private final static String TAG = FsSettings.class.getSimpleName();
    private static List<FtpUser> ftpUserList = new ArrayList<FtpUser>();    //ftp user
    private static boolean allowAnonymous = false;  //是否允许匿名登录ftp
    private static int portNum = 2121;  //ftp 端口号

    private static String externalStorageUri;   //外部存储地址

    private static void initFtpUser(){
        final Context context = App.getAppContext();
        FtpUser ftpUser = new FtpUser("ftp","ftp","\\");
        FtpUser ftpUser2 = new FtpUser("lv","lv","\\");
        FtpUser ftpUser3 = new FtpUser("wzz","wzz","/sdcard/EBSB_SDCardLog");
        ftpUserList.add(ftpUser);
        ftpUserList.add(ftpUser2);
        ftpUserList.add(ftpUser3);
    }

    public static List<FtpUser> getUsers() {
        if(ftpUserList == null || ftpUserList.size() == 0){
            initFtpUser();
        }
        return ftpUserList;
    }

    public static FtpUser getUser(String username) {
        for (FtpUser user : getUsers()) {
            if (user.getUsername().equals(username))
                return user;
        }
        return null;
    }

    public static void addUser(FtpUser user) {
        if (getUser(user.getUsername()) != null) {
            throw new IllegalArgumentException("User already exists");
        }
        ftpUserList.add(user);
    }

    public static void removeUser(String username) {

        List<FtpUser> users = getUsers();
        List<FtpUser> found = new ArrayList<FtpUser>();
        for (FtpUser user : users) {
            if (user.getUsername().equals(username)) {
                found.add(user);
            }
        }
        users.removeAll(found);
    }

    public static void modifyUser(String username, FtpUser newUser) {
        removeUser(username);
        addUser(newUser);
    }

    public static boolean allowAnonymous() {
        return allowAnonymous;
    }

    public static File getDefaultChrootDir() {
        File chrootDir;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            chrootDir = Environment.getExternalStorageDirectory();
        } else {
            chrootDir = new File("/");
        }
        if (!chrootDir.isDirectory()) {
            Log.e(TAG, "getChrootDir: not a directory");
            // if this happens, we are screwed
            // we give it the application directory
            // but this will probably not be what the user wants
            return App.getAppContext().getFilesDir();
        }

//        chrootDir = new File("/sdcard/EBSB_SDCardLog");
        return chrootDir;
    }

    public static int getPortNumber() {
        return portNum;
    }

    public static boolean shouldTakeFullWakeLock() {
        return false;
    }

    public static Set<String> getAutoConnectList() {
        return new TreeSet<String>();
    }

    public static void removeFromAutoConnectList(final String ssid) {

//        Set<String> autoConnectList = getAutoConnectList();
//        autoConnectList.remove(ssid);
//        SharedPreferences.Editor editor = getSharedPreferences().edit();
//        editor.remove("autoconnect_preference").apply(); // work around bug in android
//        editor.putStringSet("autoconnect_preference", autoConnectList).apply();
    }



    public static boolean showNotificationIcon() {
        return true;
    }

    public static String getExternalStorageUri() {
        return externalStorageUri;
    }

    public static void setExternalStorageUri(String exStorageUri) {
        externalStorageUri = exStorageUri;
    }

}
