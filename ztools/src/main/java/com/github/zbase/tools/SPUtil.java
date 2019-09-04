package com.github.zbase.tools;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.github.library.ZLog;

import java.util.Set;

/**
 * Created by wzz on 2017/07/06.
 * wuzhenzhen@tiamaes.com
 */

public class SPUtil {
    private static SharedPreferences sp = null;
    public static SPUtil bsp = null;

    public static SPUtil  getInstance(Context context){
        if(bsp == null || sp == null) {
            bsp = new SPUtil();
            sp = context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
        }
        return bsp;
    }

    public boolean contains(String key){
        return sp.contains(key);
    }

    public String getData(String key) {
        return sp.getString(key, null);
    }

    public String getData(String key, String defaultStr) {
        return sp.getString(key, defaultStr);
    }

    public int getData(String key, int defaultInt){
        return sp.getInt(key, defaultInt);
    }

    public boolean getData(String key, boolean defaultBool){
        return sp.getBoolean(key, defaultBool);
    }

    public void putData(String key, String str) {
        SharedPreferences.Editor edit = sp.edit();
        edit.remove(key);
        edit.putString(key, str);
        edit.commit();
    }

    /**
     *
     * @param key
     * @param value
     * @param format  0~3
     */
    public void putData(String key, String value, int format){
        boolean isValid = false;
        switch (format){
            case 0:     //int 型数据
                isValid = AppUtils.isValidInt(value);
                break;
            case 1:     //boolean 型数据
                isValid = AppUtils.isValidBoolean(value);
                break;
            case 2:     //ip:port 数据
                isValid = AppUtils.isValidIpPort(value);
                break;
            case 3:     //时间段  "14:27~15:27"
                isValid = AppUtils.isValidTimes(value);
                break;
        }
        if(isValid){
            SharedPreferences.Editor edit = sp.edit();
            edit.remove(key);
            edit.putString(key, value);
            edit.commit();
        }else{
            ZLog.eee("--putData--"+key+"--"+value+"--"+format);
        }
    }

    public void putData(String key, boolean value){
        SharedPreferences.Editor edit = sp.edit();
        edit.remove(key);
        edit.putBoolean(key,value);
        edit.commit();
    }

    public void putData(String key, int value) {
        SharedPreferences.Editor edit = sp.edit();
        edit.remove(key);
        edit.putInt(key,value);
        edit.commit();
    }

    public static void removeData(String key) {
        SharedPreferences.Editor edit = sp.edit();
        edit.remove(key);
        edit.commit();
    }

    public static void clearData() {
        SharedPreferences.Editor edit = sp.edit();
        edit.clear();
        edit.commit();
    }

    public boolean getDataBool(String key, boolean b) {
        return sp.getBoolean(key, b);
    }

    public void putDataBool(String key, boolean b) {
        SharedPreferences.Editor edit = sp.edit();
        edit.remove(key);
        edit.putBoolean(key, b);
        edit.commit();
    }

    public Set<String> getStringSet(String key, Set<String> sets){
        return sp.getStringSet(key,sets);
    }

    public void putStringSet(String key, Set<String> sets){
        SharedPreferences.Editor edit = sp.edit();
        edit.remove(key);
        edit.putStringSet(key, sets);
        edit.commit();
    }

    //自定 SP 文件路径
    public static void putCustome(Context context, String spName, String key, String value){
        SharedPreferences sp = context.getSharedPreferences(spName, Activity.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.remove(key);
        edit.putString(key, value);
        edit.commit();
    }
    //自定 SP 文件路径
    public static String getCustome(Context context, String spName, String key){
        SharedPreferences sp = context.getSharedPreferences(spName, Activity.MODE_PRIVATE);
        return sp.getString(key, "");
    }
}
