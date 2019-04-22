package com.github.library.flow.dbutils;


import com.github.library.flow.entity.Demo;
import com.github.library.flow.greendao.DemoDao;

import org.greenrobot.greendao.query.QueryBuilder;

public class DemoOperator extends BaseOperator<Demo> {
    @Override
    public boolean isObjectExit(long id) {
        QueryBuilder<Demo> queryBuilder = getDaoSession().getDemoDao().queryBuilder();
        queryBuilder.where(DemoDao.Properties.Id.eq(id));
        return queryBuilder.buildCount().count() > 0 ? true : false;
    }
}
