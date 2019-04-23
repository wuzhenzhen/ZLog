package com.github.library.flow.dbutils;

import com.github.library.flow.entity.TrafficDayDetail;
import com.github.library.flow.entity.TrafficDetail;
import com.github.library.flow.greendao.TrafficDayDetailDao;
import com.github.library.flow.greendao.TrafficDetailDao;

import org.greenrobot.greendao.query.QueryBuilder;

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
}
