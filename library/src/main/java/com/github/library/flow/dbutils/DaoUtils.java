package com.github.library.flow.dbutils;

import android.content.Context;


/**
 * Created by wzz on 2019/04/22.
 * kgd.zhen@gmail.com
 * 数据库操作辅助工具类
 */
public enum DaoUtils {
    INSTANCE;
    private DemoOperator mDemoOperator;
    private TrafficDetailOperator mTraficDetailOperator;
    private TrafficDayDetailOperator mTraficDayDetailOperator;


    public void init(Context context){
        BaseOperator.init(context);
    }

    public DemoOperator getDemoOperator(){
        if (mDemoOperator == null) mDemoOperator = new DemoOperator();
        return mDemoOperator;
    }

    public TrafficDetailOperator getTrafficDetailOperator(){
        if (mTraficDetailOperator == null) mTraficDetailOperator = new TrafficDetailOperator();
        return mTraficDetailOperator;
    }

    public TrafficDayDetailOperator getTrafficDayDetailOperator(){
        if (mTraficDayDetailOperator == null) mTraficDayDetailOperator = new TrafficDayDetailOperator();
        return mTraficDayDetailOperator;
    }
}
