package com.github.library.flow.dbutils;

import android.content.Context;


/**
* @date 2017/9/5
* @auther wangyt
* @version 1.0
* @describe 数据库操作辅助工具类
*/

public enum DaoUtils {
    INSTANCE;
    private DemoOperator mDemoOperator;

    public void init(Context context){
        BaseOperator.init(context);
    }

    public DemoOperator getDemoOperator(){
        if (mDemoOperator == null) mDemoOperator = new DemoOperator();
        return mDemoOperator;
    }

}
