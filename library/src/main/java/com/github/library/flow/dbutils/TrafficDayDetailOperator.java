package com.github.library.flow.dbutils;

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
        QueryBuilder<TrafficDayDetail> queryBuilder =
                getDaoSession().getTrafficDayDetailDao().queryBuilder();
        List<TrafficDayDetail> entityAngleRaws = queryBuilder
                .where(TrafficDayDetailDao.Properties.StartTime.in(getYesterdayStartTime(), getYesterdayEndTime()))
                .orderDesc(TrafficDayDetailDao.Properties.LastTime)
                .list();
        if (entityAngleRaws != null && entityAngleRaws.size() > 0){
            return  entityAngleRaws.get(0);
        }
        return null;
    }

    /**
     *
     * @return  返回昨天的开始时间 long （如 2019-04-24 00:00:00）
     */
    public static long getYesterdayStartTime(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)-1);
        calendar.set(Calendar.HOUR,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        System.out.println("开始时间："+calendar.getTime());
        return calendar.getTimeInMillis();
    }

    /**
     *
     * @return  返回昨天的结束时间 long （如 2019-04-24 23:59:59）
     */
    public static long getYesterdayEndTime(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)-1);
        calendar.set(Calendar.HOUR,23);
        calendar.set(Calendar.MINUTE,59);
        calendar.set(Calendar.SECOND,59);
        calendar.set(Calendar.MILLISECOND,999);
        System.out.println("结束时间："+calendar.getTime());
        return calendar.getTimeInMillis();
    }

}
