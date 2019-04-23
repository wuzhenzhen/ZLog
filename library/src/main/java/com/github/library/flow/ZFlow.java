package com.github.library.flow;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.support.annotation.NonNull;

import com.github.library.ZLog;
import com.github.library.flow.dbutils.DaoUtils;
import com.github.library.flow.entity.TrafficDetail;

import java.util.List;

/**
 * Created by wzz on 2019/04/22.
 * kgd.zhen@gmail.com
 */
public class ZFlow {
    //1 级 Tag
    public static final String TAG = "ZFlow";
    //Application Context 防止内存泄露
    private static Context mContext;
    private final static long startTime =  System.currentTimeMillis(); //开机时间

    public static void initialize(@NonNull Context context) {
        mContext = context.getApplicationContext();
        //--------数据库初始化
        DaoUtils.INSTANCE.init(mContext);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(30*1000L);
                        // 获取到配置权限信息的应用程序
                        PackageManager pms = context.getPackageManager();
                        List<PackageInfo> packinfos = pms
                                .getInstalledPackages(PackageManager.GET_PERMISSIONS);
                        for (PackageInfo packinfo : packinfos) {
                            boolean isPrint = false;
                            // 获取该应用的所有权限信息
                            String[] permissions = packinfo.requestedPermissions;
                            if (permissions != null && permissions.length > 0) {
                                for (String permission : permissions) {
                                    // 筛选出具有Internet权限的应用程序
                                    if ("android.permission.INTERNET".equals(permission)) {
                                        long lastTime = System.currentTimeMillis();
                                        String packageName = packinfo.packageName;
                                        int uid = packinfo.applicationInfo.uid;
                                        long uidRX = TrafficStats.getUidRxBytes(packinfo.applicationInfo.uid);
                                        long uidTX = TrafficStats.getUidTxBytes(packinfo.applicationInfo.uid);
                                        long mobileRX = TrafficStats.getMobileRxBytes();
                                        long mobileTX = TrafficStats.getMobileTxBytes();
                                        long totalRX = TrafficStats.getTotalRxBytes();
                                        long totalTX = TrafficStats.getTotalTxBytes();
                                        long total = totalRX + totalTX;
                                        if (uidRX > 0 || uidTX > 0) {
                                            TrafficDetail td = new TrafficDetail();
                                            td.setPackageName(packageName);
                                            td.setStartTime(startTime);
                                            td.setUid(uid);
                                            td.setUidRX(uidRX);
                                            td.setUidTX(uidTX);

                                            td.setMobileRX(mobileRX);
                                            td.setMobileTX(mobileTX);
                                            td.setTotalRX(totalRX);
                                            td.setTotalTX(totalTX);
                                            td.setTotal(total);
                                            td.setLastTime(System.currentTimeMillis());
                                            ZLog.ddd("--traffic--"+td.toString());
                                            DaoUtils.INSTANCE.getTrafficDetailOperator().insertObject(td);
                                        }
                                    }
                                }
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * 返回所有的有互联网访问权限的应用程序的流量信息。 只统计本次开机所用流量(每次开机从0统计流量的 )
     *
     * @param context
     * @param type    0 只打印本app; 1 打印uidRx >0 && uidTx>0; 2 打印所有;
     */
    public static void printTrafficInfo(Context context, int type) {
        // http://blog.csdn.net/u010402982/article/details/50424084
        ZLog.ddd("*******************TrafficStats*START*****************");

        // 获取到配置权限信息的应用程序
        PackageManager pms = context.getPackageManager();
        List<PackageInfo> packinfos = pms
                .getInstalledPackages(PackageManager.GET_PERMISSIONS);
        for (PackageInfo packinfo : packinfos) {
            boolean isPrint = false;
            // 获取该应用的所有权限信息
            String[] permissions = packinfo.requestedPermissions;
            if (permissions != null && permissions.length > 0) {
                for (String permission : permissions) {
                    // 筛选出具有Internet权限的应用程序
                    if ("android.permission.INTERNET".equals(permission)) {
                        if (type == 0) { // 只打印本app的
                            if (packinfo.packageName.equals(context
                                    .getPackageName())) {
                                isPrint = true;
                            } else {
                                continue;
                            }
                        } else if (type == 1) { // 打印uidRx >0 && uidTx>0
                            if (TrafficStats
                                    .getUidRxBytes(packinfo.applicationInfo.uid) > 0
                                    && TrafficStats
                                    .getUidTxBytes(packinfo.applicationInfo.uid) > 0) {
                                isPrint = true;
                            } else {
                                continue;
                            }
                        } else if (type == 2) { // 打印所有的
                            isPrint = true;
                        }
                        // String key =
                        // AppUtils.getDateStrFromLong(System.currentTimeMillis(),"yyyy-MM-dd");
                        String value = "--"
                                + packinfo.packageName
                                + "--"
                                + packinfo.applicationInfo.loadLabel(pms)
                                .toString()
                                + "--"
                                + packinfo.applicationInfo.uid
                                + "--uidRx="
                                + TrafficStats
                                .getUidRxBytes(packinfo.applicationInfo.uid)
                                + "--uidTx="
                                + TrafficStats
                                .getUidTxBytes(packinfo.applicationInfo.uid)
                                + "--MobileRx="
                                + TrafficStats.getMobileRxBytes()
                                + "--MobileTx="
                                + TrafficStats.getMobileTxBytes()
                                + "--TotalRx=" + TrafficStats.getTotalRxBytes()
                                + "--TotalTx=" + TrafficStats.getTotalTxBytes();

                        if (isPrint)
                            ZLog.iii(value);
                        // //控制台+写入Log 文件,且指定 Log文件名
                        // Logcat.fvv(key+value, "2017-10-31-tra.log");
                        break;
                    }
                }
            }
        }
        ZLog.ddd("*******************TrafficStats*END*******************");
    }

}
