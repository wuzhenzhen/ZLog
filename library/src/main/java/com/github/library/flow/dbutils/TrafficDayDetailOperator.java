package com.github.library.flow.dbutils;

import android.database.Cursor;

import com.github.library.flow.entity.TrafficDayDetail;
import com.github.library.flow.greendao.TrafficDayDetailDao;

import org.greenrobot.greendao.query.QueryBuilder;
import java.util.Calendar;
import java.util.List;

/**
 * Created by wzz on 2019/04/23.
 * kgd.zhen@gmail.com
 * 数据库实例操作类
 */
public class TrafficDayDetailOperator extends BaseOperator<TrafficDayDetail>  {

    @Override
    public boolean isObjectExit(long id) {
        QueryBuilder< TrafficDayDetail> queryBuilder = getDaoSession().getTrafficDayDetailDao().queryBuilder();
        queryBuilder.where(TrafficDayDetailDao.Properties.Id.eq(id));
        return queryBuilder.buildCount().count() > 0 ? true : false;
    }

    //
    public TrafficDayDetail queryByStartTime(long startTime, String packageName) {
        QueryBuilder<TrafficDayDetail> queryBuilder =
                getDaoSession().getTrafficDayDetailDao().queryBuilder();
        List<TrafficDayDetail> entityAngleRaws = queryBuilder
                .where(TrafficDayDetailDao.Properties.StartTime.eq(startTime), TrafficDayDetailDao.Properties.PackageName.eq(packageName))
                .orderAsc(TrafficDayDetailDao.Properties.LastTime)
                .list();
        if (entityAngleRaws != null && entityAngleRaws.size() > 0){
            return  entityAngleRaws.get(0);
        }
        return null;
    }

    //获取最后插入的一条数据
    public TrafficDayDetail queryByLastTime() {
        QueryBuilder<TrafficDayDetail> queryBuilder =
                getDaoSession().getTrafficDayDetailDao().queryBuilder();
        List<TrafficDayDetail> entityAngleRaws = queryBuilder
                .orderDesc(TrafficDayDetailDao.Properties.LastTime)
                .list();
        if (entityAngleRaws != null && entityAngleRaws.size() > 0){
            return  entityAngleRaws.get(0);
        }
        return null;
    }

    //获取昨天最后一条数据
    public TrafficDayDetail queryByYesterday() {
        long currentTime = System.currentTimeMillis();
        QueryBuilder<TrafficDayDetail> queryBuilder =
                getDaoSession().getTrafficDayDetailDao().queryBuilder();
        List<TrafficDayDetail> entityAngleRaws = queryBuilder
                .where(TrafficDayDetailDao.Properties.StartTime.ge(getStartTime(currentTime, -1)),TrafficDayDetailDao.Properties.StartTime.le(getEndTime(currentTime,-1)))
                .orderDesc(TrafficDayDetailDao.Properties.LastTime)
                .list();
        if (entityAngleRaws != null && entityAngleRaws.size() > 0){
            return  entityAngleRaws.get(0);
        }
        return null;
    }

    // 查询某天的数据总流量
    public long querySumFlow(long timeMillis){
        String sql = "select sum(TOTAL) sumt FROM TRAFFIC_DAY_DETAIL WHERE START_TIME >= ? and START_TIME <= ?";
        String startTime = String.valueOf(getStartTime(timeMillis,0));
        String endTime = String.valueOf(getEndTime(timeMillis,0));
        Cursor c = getDaoSession().getTrafficDayDetailDao().getDatabase().rawQuery(sql, new String[]{startTime, endTime});
        long sumFlow = 0;
        try{
            if (c.moveToFirst()) {
                do {
                    sumFlow = c.getLong(0);
                } while (c.moveToNext());
            }
        } finally {
            c.close();
        }
        System.out.println("###sumFlow###"+sumFlow);
        return sumFlow;
    }

    // 查询昨天使用的总流量情况
    public long querySumFlowYesterday(){
        long currentTime = System.currentTimeMillis();
        String startTime = String.valueOf(getStartTime(currentTime, -1));
        String endTime = String.valueOf(getEndTime(currentTime, -1));
//        String sql = "select count(TOTAL) countt, sum(TOTAL) sumt from TRAFFIC_DAY_DETAIL where start_time >= 1556035200000 and start_time <= 1556121599000";
        String sql = "select sum(TOTAL) sumt FROM TRAFFIC_DAY_DETAIL WHERE START_TIME >= ? and START_TIME <= ?";
        Cursor c = getDaoSession().getTrafficDayDetailDao().getDatabase().rawQuery(sql, new String[]{startTime, endTime});
        long sumFlow = 0;
        try{
            if (c.moveToFirst()) {
                do {
                    sumFlow = c.getLong(0);
                } while (c.moveToNext());
            }
        } finally {
            c.close();
        }
        System.out.println("###sumFlow###"+sumFlow);
//        sumFlow = 0;
//        QueryBuilder<TrafficDayDetail> queryBuilder =
//                getDaoSession().getTrafficDayDetailDao().queryBuilder();
//        List<TrafficDayDetail> entityAngleRaws = queryBuilder
////                .where(TrafficDayDetailDao.Properties.StartTime.in(getStartTime(currentTime), getEndTime(currentTime)))
//                .where(TrafficDayDetailDao.Properties.StartTime.ge(getStartTime(currentTime)),TrafficDayDetailDao.Properties.StartTime.le(getEndTime(currentTime)))
//                .orderDesc(TrafficDayDetailDao.Properties.LastTime)
//                .list();
//
//        if (entityAngleRaws != null && entityAngleRaws.size() > 0){
//            for(TrafficDayDetail tdd : entityAngleRaws){
//                sumFlow += tdd.getTotal();
//            }
//        }
        return sumFlow;
    }

    /**
     *   返回某天的开始时间 long （如 2019-04-24 00:00:00）
     *   如： getStartTime(System.currentTimeMillis(),-1);    //返回昨天的开始时间
     *        getStartTime(System.currentTimeMillis(),0);    //返回当天的开始时间
     * @param timeMillis
     * @param day
     * @return
     */
    public static long getStartTime(long timeMillis, int day){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeMillis);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH + day));
        calendar.set(Calendar.HOUR,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        System.out.println("开始时间："+calendar.getTime());
        return calendar.getTimeInMillis();
    }

    /**
     * 返回某天的结束时间 long （如 2019-04-24 23:59:59）
     * @param timeMillis
     * @param day
     * @return
     *   如： getEndTime(System.currentTimeMillis(),-1);    //返回昨天的结束时间
     *        getEndTime(System.currentTimeMillis(),0);    //返回当天的结束时间
     */
    public static long getEndTime(long timeMillis, int day){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeMillis);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH + day));
        calendar.set(Calendar.HOUR,23);
        calendar.set(Calendar.MINUTE,59);
        calendar.set(Calendar.SECOND,59);
        calendar.set(Calendar.MILLISECOND,999);
        System.out.println("结束时间："+calendar.getTime());
        return calendar.getTimeInMillis();
    }

}
