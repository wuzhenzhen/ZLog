package com.github.library.flow;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.support.annotation.NonNull;

import com.github.library.ZLog;
import com.github.library.flow.dbutils.DaoUtils;
import com.github.library.flow.entity.TrafficDayDetail;
import com.github.library.flow.entity.TrafficDetail;

import java.text.SimpleDateFormat;
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
    private static long startTime =  System.currentTimeMillis(); //开机时间

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
                                            td.setLastTime(lastTime);
                                            ZLog.iii("--traffic--"+td.toString());
                                            // 保存详细流量数据
                                            DaoUtils.INSTANCE.getTrafficDetailOperator().insertObject(td);

                                            // 更新 trafficDayDetail
                                            updateTrafficDyaDetail(totalRX, totalTX, total);
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

    // 更新 trafficDayDetail
    public static void updateTrafficDyaDetail(long totalRX, long totalTX, long total){
        //更新 startTime(开机时间)  到现在的流量
        TrafficDayDetail tdd = DaoUtils.INSTANCE.getTrafficDayDetailOperator().queryByLastTime();
        if(tdd == null){  //一条数据也没有
            tdd = new TrafficDayDetail();
            tdd.setStartTime(startTime);
            tdd.setRx(totalRX);
            tdd.setTx(totalTX);
            tdd.setTotal(total);
            tdd.setLastTime(System.currentTimeMillis());
            DaoUtils.INSTANCE.getTrafficDayDetailOperator().insertObject(tdd);
        }else{
            String lastYMD = getYMDFromLong(tdd.getStartTime());  //数据库 里最后一条记录的时间
            String startTimeYMD = getYMDFromLong(startTime);  //开机时间
            String nowYMD = getYMDFromLong(System.currentTimeMillis()); //当前时间
            if(lastYMD.equals(startTimeYMD) && lastYMD.equals(nowYMD)){  //同一天
                if(startTime == tdd.getStartTime()){ //此记录已存在, 且startTime 一样
                    //继续更新数据
                    tdd.setRx(totalRX);
                    tdd.setTx(totalTX);
                    tdd.setTotal(total);
                    tdd.setLastTime(System.currentTimeMillis());
                    DaoUtils.INSTANCE.getTrafficDayDetailOperator().insertObject(tdd);
                }else{  // 说明 存在程序重启或关机重启情况
                    if(total > tdd.getTotal()){  //说明是程序重启了
                        startTime = System.currentTimeMillis();
                        //创建新数据
                        tdd.setStartTime(startTime);
                        tdd.setRx(totalRX);
                        tdd.setTx(totalTX);
                        tdd.setTotal(total);
                        tdd.setLastTime(System.currentTimeMillis());
                        DaoUtils.INSTANCE.getTrafficDayDetailOperator().insertObject(tdd);
                    }else{  //说明系统重启了， 流量清零了
                        //重新插入一条记录
                        tdd = new TrafficDayDetail();
                        tdd.setStartTime(startTime);

                        tdd.setRx(totalRX);
                        tdd.setTx(totalTX);
                        tdd.setTotal(total);
                        tdd.setLastTime(System.currentTimeMillis());
                        DaoUtils.INSTANCE.getTrafficDayDetailOperator().insertObject(tdd);
                    }
                }
            }else if (!lastYMD.equals(startTimeYMD)){ // 最后一次记录时间 与 开机时间不致，说明是第二天运行的
                //重新插入一条记录
                tdd = new TrafficDayDetail();
                tdd.setStartTime(startTime);

                tdd.setRx(totalRX);
                tdd.setTx(totalTX);
                tdd.setTotal(total);
                tdd.setLastTime(System.currentTimeMillis());
                DaoUtils.INSTANCE.getTrafficDayDetailOperator().insertObject(tdd);
            }else if (lastYMD.equals(startTimeYMD) && !startTimeYMD.equals(nowYMD)){ //隔天运行了
                //昨天开机一直运行到今天
                //考虑每次入库时要减去 前一次的数据
                TrafficDayDetail tddYes = DaoUtils.INSTANCE.getTrafficDayDetailOperator().queryByYesterday();
                if(tddYes != null && total > tddYes.getTotal()){
                    tdd = new TrafficDayDetail();
                    startTime = System.currentTimeMillis();
                    tdd.setStartTime(startTime);
                    tdd.setRx(totalRX - tddYes.getRx());
                    tdd.setTx(totalTX - tddYes.getTx());
                    tdd.setTotal(total - tddYes.getTotal());
                    tdd.setLastTime(System.currentTimeMillis());
                    DaoUtils.INSTANCE.getTrafficDayDetailOperator().insertObject(tdd);
                }else{
                    ZLog.eee("--error--");
                }
            }
        }
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

    // Long时间格式化
    public static String getYMDHMSFromLong(Long date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        java.util.Date dt = new java.util.Date(date);
        String sDateTime = df.format(dt);
        return sDateTime;
    }

    // Long时间格式化
    public static String getYMDFromLong(Long date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        java.util.Date dt = new java.util.Date(date);
        String sDateTime = df.format(dt);
        return sDateTime;
    }

    //返回昨天的总流量
    public static long getSumFlowYesterday(){
        return DaoUtils.INSTANCE.getTrafficDayDetailOperator().querySumFlowYesterday();
    }

    //返回某天的总流量
    public static long getSumFlow(long timeMillis){
        return DaoUtils.INSTANCE.getTrafficDayDetailOperator().querySumFlow(timeMillis);
    }
}


