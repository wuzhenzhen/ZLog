package com.github.library.flow.dbutils;

import com.github.library.flow.entity.TrafficDetail;
import com.github.library.flow.greendao.TrafficDetailDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by wzz on 2019/04/23.
 * wuzhenzhen@tiamaes.com
 * 数据库实例操作类
 */
public class TrafficDetailOperator extends BaseOperator<TrafficDetail> {
    @Override
    public boolean isObjectExit(long id) {
        QueryBuilder<TrafficDetail> queryBuilder = getDaoSession().getTrafficDetailDao().queryBuilder();
        queryBuilder.where(TrafficDetailDao.Properties.Id.eq(id));
        return queryBuilder.buildCount().count() > 0 ? true : false;
    }

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
}
