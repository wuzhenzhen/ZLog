package com.github.library.flow.dbutils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.github.library.flow.greendao.DaoMaster;
import com.github.library.flow.greendao.DaoSession;

import org.greenrobot.greendao.query.QueryBuilder;


/**
* @date 2017/9/5
* @auther wangyt
* @version 1.0
* @describe 数据库管理类
*/

public enum DaoManager {
    INSTANCE;

    private String DB_NAME;

    private DaoMaster.DevOpenHelper devOpenHelper;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private SQLiteDatabase database;
    private Context context;

    DaoManager(){
    }

    public void init(Context context){
        this.context = context.getApplicationContext();
    }

    public DaoMaster getDaoMaster(){
        if (null == daoMaster){
            DB_NAME = "manicure.db";
            devOpenHelper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
            daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
        }
        return daoMaster;
    }

    public DaoSession getDaoSession(){
        if (null == daoSession){
            if (null == daoMaster){
                daoMaster = getDaoMaster();
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }

    public void setDebug(boolean flag){
        QueryBuilder.LOG_SQL = flag;
        QueryBuilder.LOG_VALUES = flag;
    }

    public void closeDaoSession(){
        if (null != daoSession){
            daoSession.clear();
            daoSession = null;
        }
    }

    public void closeDevOpenHelper(){
        if (null != devOpenHelper){
            devOpenHelper.close();
            devOpenHelper = null;
        }
    }

    public void closeDB(){
        closeDevOpenHelper();
        closeDaoSession();
    }
}
