package com.github.library.flow.dbutils;

import android.content.Context;


import com.github.library.flow.greendao.DaoSession;

import java.util.List;


/**
 * Created by wzz on 2019/04/22.
 * kgd.zhen@gmail.com
 * 数据操作基类
 */
public abstract class BaseOperator<T> {
    private String TAG = "<<<<<<" + getClass().getSimpleName();

    private DaoSession daoSession;

    public static void init(Context context){
        DaoManager.INSTANCE.init(context);
    }

    public BaseOperator(){
        daoSession = DaoManager.INSTANCE.getDaoSession();
        DaoManager.INSTANCE.setDebug(false);
    }

    public DaoSession getDaoSession(){
        return daoSession;
    }

    public void insertObject(T object){
        if (null == object) return;
        daoSession.insertOrReplace(object);
    }

    public void insertObjectSet(final List<T> objects){
        if (null == objects || objects.isEmpty()) return;
        daoSession.runInTx(new Runnable() {
            @Override
            public void run() {
                for (T object : objects){
                    daoSession.insertOrReplace(object);
                }
            }
        });
    }

    public void updateObject(T object){
        if (null == object) return;
        daoSession.update(object);
    }

    public void updateObjectSet(final List<T> objects, Class clss){
        if (null == objects || objects.isEmpty()) return;
        daoSession.getDao(clss).updateInTx(new Runnable() {
            @Override
            public void run() {
                for (T object : objects){
                    daoSession.update(object);
                }
            }
        });
    }

    public void deleteAll(Class clss){
        daoSession.deleteAll(clss);
    }

    public void deleteObject(T object){
        daoSession.delete(object);
    }

    public void deleteObjectSet(final List<T> objects, Class clss){
        if (null == objects || objects.isEmpty()) return;
        daoSession.getDao(clss).deleteInTx(new Runnable() {
            @Override
            public void run() {
                for (T object : objects){
                    daoSession.delete(object);
                }
            }
        });
    }

    public String getTableName(Class clss){
        return daoSession.getDao(clss).getTablename();
    }

    public abstract boolean isObjectExit(long id);

    public T queryByKey(long id, Class clss){
        return (T) daoSession.getDao(clss).load(id);
    }

    public T queryById(long id, Class clss){
        return (T) daoSession.getDao(clss).loadByRowId(id);
    }

    public List<T> query(Class clss, String where, String... params){
        return (List<T>) daoSession.getDao(clss).queryRaw(where, params);
    }

    public List<T> queryAll(Class clss){
        return (List<T>) daoSession.getDao(clss).loadAll();
    }

    public void closeDB(){
        DaoManager.INSTANCE.closeDB();
    }
}
