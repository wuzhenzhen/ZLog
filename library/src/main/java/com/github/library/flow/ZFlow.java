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
    //1 �� Tag
    public static final String TAG = "ZFlow";
    //Application Context ��ֹ�ڴ�й¶
    private static Context mContext;
    private static long startTime =  System.currentTimeMillis(); //����ʱ��
    private static boolean isRunMoreDay = false;  //�Ƿ����г���һ��

    public static void initialize(@NonNull Context context) {
        mContext = context.getApplicationContext();
        //--------���ݿ��ʼ��
        DaoUtils.INSTANCE.init(mContext);
        clearCache();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(30*1000L);
                        // ��ȡ������Ȩ����Ϣ��Ӧ�ó���
                        PackageManager pms = context.getPackageManager();
                        List<PackageInfo> packinfos = pms
                                .getInstalledPackages(PackageManager.GET_PERMISSIONS);
                        for (PackageInfo packinfo : packinfos) {
                            boolean isPrint = false;
                            // ��ȡ��Ӧ�õ�����Ȩ����Ϣ
                            String[] permissions = packinfo.requestedPermissions;
                            if (permissions != null && permissions.length > 0) {
                                for (String permission : permissions) {
                                    // ɸѡ������InternetȨ�޵�Ӧ�ó���
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
//                                            ZLog.ddd("--traffic--"+td.toString());
                                            // ������ϸ��������
                                            DaoUtils.INSTANCE.getTrafficDetailOperator().insertObject(td);

                                            // ���� trafficDayDetail
                                            updateTrafficDyaDetail(totalRX, totalTX, total);
                                        }
                                    }
                                }
                            }
                        }
                    } catch (InterruptedException e) {
                        ZLog.eee("--ZFlow--InterruptedException--"+e.getLocalizedMessage());
                        e.printStackTrace();
                    } catch (Exception e){
                        ZLog.eee("--ZFlow--Exception--"+e.getLocalizedMessage());
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    //���ݿ⻺������
    private static void clearCache(){
        DaoUtils.INSTANCE.getTrafficDetailOperator().delBeforeTime(10); //��� 10��֮ǰ�� ������ϸ����
        DaoUtils.INSTANCE.getTrafficDayDetailOperator().delBeforeTime(100); //���100��֮ǰ������ͳ������
    }

    // ���� trafficDayDetail
    public static void updateTrafficDyaDetail(long totalRX, long totalTX, long total){
        //���� startTime(����ʱ��)  �����ڵ�����
        TrafficDayDetail tdd = DaoUtils.INSTANCE.getTrafficDayDetailOperator().queryByLastTime();
        if(tdd == null){  //�������У�һ������Ҳû��
            tdd = new TrafficDayDetail();
            tdd.setStartTime(startTime);
            tdd.setRx(totalRX);
            tdd.setTx(totalTX);
            tdd.setTotal(total);
            tdd.setLastTime(System.currentTimeMillis());
            DaoUtils.INSTANCE.getTrafficDayDetailOperator().insertObject(tdd);
            ZLog.iii("--program first start--");
        }else{
            String lastYMD = getYMDFromLong(tdd.getStartTime());  //���ݿ� �����һ����¼�� ������ ʱ��
            String startTimeYMD = getYMDFromLong(startTime);  //���ο�����  ������ ʱ��
            String nowYMD = getYMDFromLong(System.currentTimeMillis()); //��ǰ ������ ʱ��
            if(lastYMD.equals(startTimeYMD)){  //���һ����¼ �� ���ο�����ͬһ��
                if(lastYMD.equals(nowYMD)){  //ͬһ��
                    if(startTime == tdd.getStartTime()){ //�˼�¼�Ѵ���, ��startTime һ��    ������������
                        if(isRunMoreDay){
                            TrafficDayDetail tddYes = DaoUtils.INSTANCE.getTrafficDayDetailOperator().queryByYesterday();
                            if(tddYes != null){
                                tdd.setRx(totalRX - tddYes.getRx());
                                tdd.setTx(totalTX - tddYes.getTx());
                                tdd.setTotal(total - tddYes.getTotal());
                            }else {
                                ZLog.eee("--impossibility error--tddYes is null--1");
                            }
                        }else{
                            tdd.setRx(totalRX);
                            tdd.setTx(totalTX);
                            tdd.setTotal(total);
                        }
                        tdd.setLastTime(System.currentTimeMillis());
                        DaoUtils.INSTANCE.getTrafficDayDetailOperator().insertObject(tdd);
                    }else{  // ˵�� ���ڳ���������ػ���������� ��Ҫ������startTime ����ʱ��
                        if(total > tdd.getTotal()){  //˵���ǳ���������
                            startTime = System.currentTimeMillis();
                            //����������
                            tdd.setStartTime(startTime);
                            if(isRunMoreDay){
                                TrafficDayDetail tddYes = DaoUtils.INSTANCE.getTrafficDayDetailOperator().queryByYesterday();
                                if(tddYes != null){
                                    tdd.setRx(totalRX - tddYes.getRx());
                                    tdd.setTx(totalTX - tddYes.getTx());
                                    tdd.setTotal(total - tddYes.getTotal());
                                }else {
                                    ZLog.eee("--impossibility error--tddYes is null--2");
                                }
                            }else{
                                tdd.setRx(totalRX);
                                tdd.setTx(totalTX);
                                tdd.setTotal(total);
                            }
                            tdd.setLastTime(System.currentTimeMillis());
                            DaoUtils.INSTANCE.getTrafficDayDetailOperator().insertObject(tdd);
                            ZLog.iii("--program reboot--");
                        }else{  //˵��ϵͳ�����ˣ� ����������
                            //���²���һ����¼
                            tdd = new TrafficDayDetail();
                            tdd.setStartTime(startTime);
                            tdd.setRx(totalRX);
                            tdd.setTx(totalTX);
                            tdd.setTotal(total);
                            tdd.setLastTime(System.currentTimeMillis());
                            DaoUtils.INSTANCE.getTrafficDayDetailOperator().insertObject(tdd);
                            ZLog.iii("--system reboot--");
                        }
                    }
                }else{  //���쿪��һֱ���е�����
                    //����ÿ�����ʱҪ��ȥ ǰһ�ε�����
                    TrafficDayDetail tddYes = DaoUtils.INSTANCE.getTrafficDayDetailOperator().queryByYesterday();
                    if(tddYes != null && total > tddYes.getTotal()){
                        tdd = new TrafficDayDetail();
                        isRunMoreDay = true;
                        startTime = System.currentTimeMillis();
                        tdd.setStartTime(startTime);
                        tdd.setRx(totalRX - tddYes.getRx());
                        tdd.setTx(totalTX - tddYes.getTx());
                        tdd.setTotal(total - tddYes.getTotal());
                        tdd.setLastTime(System.currentTimeMillis());
                        DaoUtils.INSTANCE.getTrafficDayDetailOperator().insertObject(tdd);
                        ZLog.iii("--program run more day--tddYes="+tddYes.toString()+"--"+tdd.toString());
                    }else{
                        ZLog.eee("--impossibility error--");
                    }
                }
            }else { // ���һ�μ�¼ʱ�� �� ����ʱ�䲻�£�˵���ǵڶ��쿪�����е�
                //���²���һ����¼
                tdd = new TrafficDayDetail();
                tdd.setStartTime(startTime);

                tdd.setRx(totalRX);
                tdd.setTx(totalTX);
                tdd.setTotal(total);
                tdd.setLastTime(System.currentTimeMillis());
                DaoUtils.INSTANCE.getTrafficDayDetailOperator().insertObject(tdd);
                ZLog.iii("--program run another day--");
            }
        }
    }

    // Longʱ���ʽ��
    public static String getYMDHMSFromLong(Long date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        java.util.Date dt = new java.util.Date(date);
        String sDateTime = df.format(dt);
        return sDateTime;
    }

    // Longʱ���ʽ��
    public static String getYMDFromLong(Long date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        java.util.Date dt = new java.util.Date(date);
        String sDateTime = df.format(dt);
        return sDateTime;
    }

    //���������������
    public static long getSumFlowYesterday(){
        return DaoUtils.INSTANCE.getTrafficDayDetailOperator().querySumFlowYesterday();
    }

    //����ĳ���������
    public static long getSumFlow(long timeMillis){
        return DaoUtils.INSTANCE.getTrafficDayDetailOperator().querySumFlow(timeMillis);
    }
}


