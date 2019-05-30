package com.github.library.flow.dbutils;

import com.github.library.flow.entity.TrafficDetail;
import com.github.library.flow.greendao.TrafficDetailDao;

import org.greenrobot.greendao.query.DeleteQuery;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by wzz on 2019/04/23.
 * wuzhenzhen@tiamaes.com
 *  流量明细数据[TrafficDetail] 数据库实例操作类
 */
public class TrafficDetailOperator extends BaseOperator<TrafficDetail> {
    @Override
    public boolean isObjectExit(long id) {
        QueryBuilder<TrafficDetail> queryBuilder = getDaoSession().getTrafficDetailDao().queryBuilder();
        queryBuilder.where(TrafficDetailDao.Properties.Id.eq(id));
        return queryBuilder.buildCount().count() > 0 ? true : false;
    }

    /**
     *
     * @param startTime
     * @return
     */
    public List<TrafficDetail> queryByStartTime(long startTime) {
        QueryBuilder<TrafficDetail> queryBuilder =
                getDaoSession().getTrafficDetailDao().queryBuilder();
        List<TrafficDetail> entityAngleRaws = queryBuilder
                .where(TrafficDetailDao.Properties.StartTime.eq(startTime))
                .orderAsc(TrafficDetailDao.Properties.StartTime)
                .list();
        if (entityAngleRaws != null && entityAngleRaws.size() > 0){
            return  entityAngleRaws;
        }
        return null;
    }

    /**
     *  删除 几天 之前的数据，
     * @param day
     */
    public void delBeforeTime(int day){
        QueryBuilder<TrafficDetail> queryBuilder =
                getDaoSession().getTrafficDetailDao().queryBuilder();
        long delTime = System.currentTimeMillis() - (day * 24 * 60 * 60 * 1000L);
        DeleteQuery<TrafficDetail> deleteQuery = queryBuilder.where(TrafficDetailDao.Properties.StartTime.le(delTime)).buildDelete();
        deleteQuery.executeDeleteWithoutDetachingEntities();
    }
}
