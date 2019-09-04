package com.github.zbase.tools;

import android.app.AlarmManager;
import android.content.Context;
import android.provider.Settings;

import com.github.library.ZLog;

import java.util.Calendar;
import java.util.TimeZone;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by wzz on 2018/06/26.
 * wuzhenzhen@tiamaes.com
 */
public class SystemUtils {

    private SystemUtils(){
        /**cannot be instantiated **/
        throw new UnsupportedOperationException("cannot be instantiated");
    }


    //---------------------------------时间相关设置-------------------------------
    /**
     *  判断系统的时区是否是自动获取的
     * @param context
     * @return
     */
    public static boolean isTimeZoneAuto(Context context){
        try {
            return  Settings.Global.getInt(context.getContentResolver(),
                    Settings.Global.AUTO_TIME_ZONE) > 0;
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     *  设置系统时区是否自动同步
     * @param context
     * @param checked  0 取消自动同步， 1自动同步
     */
    public static void setAutoTimeZone(Context context, int checked){
        Settings.Global.putInt(context.getContentResolver(),
                Settings.Global.AUTO_TIME_ZONE, checked);
    }

    /**
     *  判断系统的时间是否自动获取的
     * @param context
     * @return
     */
    public static boolean isDateTimeAuto(Context context){
        try {
            return Settings.Global.getInt(context.getContentResolver(),
                    Settings.Global.AUTO_TIME) > 0;
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     *  设置系统的时间是否需要自动获取
     * @param context
     * @param checked  0 取消自动同步， 1自动同步
     */
    public static void setAutoDateTime(Context context, int checked){
        Settings.Global.putInt(context.getContentResolver(),
                Settings.Global.AUTO_TIME, checked);
    }

    /**
     * 设置系统日期
     * @param context
     * @param cl
     */
    public static void setSysDate(Context context, Calendar cl){
        setSysDate(context,
                cl.get(Calendar.YEAR),
                cl.get(Calendar.MONTH),
                cl.get(Calendar.DAY_OF_MONTH),
                cl.get(Calendar.HOUR_OF_DAY),
                cl.get(Calendar.MINUTE),
                cl.get(Calendar.SECOND));
    }

    /**
     * 设置系统日期
     * @param context
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param minute
     * @param second
     */
    public static void setSysDate(Context context, int year, int month, int day, int hour, int minute, int second){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, second);

        long when = c.getTimeInMillis();

        if(when / 1000 < Integer.MAX_VALUE){
            ((AlarmManager)context.getSystemService(Context.ALARM_SERVICE)).setTime(when);
        }
    }

    /**
     * 设置系统时区
     * @param timeZone
     *   如： setTimeZone("GMT+8:00");
     */
    public static void setTimeZone(String timeZone){
        final Calendar now = Calendar.getInstance();
        TimeZone tz = TimeZone.getTimeZone(timeZone);
        now.setTimeZone(tz);
    }

    /**
     *  设置系统时区
     * @param context
     * @param timeZone
     *   如 "Asia/Shanghai" == 中国标准时间
     *      "Europe/London" == 格林尼治标准时间 (伦敦)
     */
    public static void setTimeZone(Context context, String timeZone){
        AlarmManager alarmManager= (AlarmManager)context.getSystemService(ALARM_SERVICE);
        alarmManager.setTimeZone(timeZone);
    }

    /**
     * 获取系统当前的时区
     * @return
     */
    public static String getDefaultTimeZone(){
        ZLog.iii("--getDefaultTimeZone--"+ TimeZone.getDefault());
        ZLog.iii("--getDefaultTimeZone--"+ TimeZone.getDefault().getID());
        return TimeZone.getDefault().getDisplayName();
    }

    /**
     *  设置24小时时间制
     * @param context
     */
    public static void set24H(Context context){
        Settings.System.putString(context.getContentResolver(),
                Settings.System.TIME_12_24, "24");
    }

    /**
     * 设置12小时时间制
     * @param context
     */
    public static void set12H(Context context){
        Settings.System.putString(context.getContentResolver(),
                Settings.System.TIME_12_24, "12");
    }

    //---------------------------------时间相关设置------END-------------------------


}
