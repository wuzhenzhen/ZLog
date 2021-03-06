package com.github.library.flow.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.github.library.flow.entity.TrafficDayDetail;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "TRAFFIC_DAY_DETAIL".
*/
public class TrafficDayDetailDao extends AbstractDao<TrafficDayDetail, Long> {

    public static final String TABLENAME = "TRAFFIC_DAY_DETAIL";

    /**
     * Properties of entity TrafficDayDetail.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property StartTime = new Property(1, long.class, "startTime", false, "START_TIME");
        public final static Property PackageName = new Property(2, String.class, "packageName", false, "PACKAGE_NAME");
        public final static Property Rx = new Property(3, long.class, "rx", false, "RX");
        public final static Property Tx = new Property(4, long.class, "tx", false, "TX");
        public final static Property Total = new Property(5, long.class, "total", false, "TOTAL");
        public final static Property LastTime = new Property(6, long.class, "lastTime", false, "LAST_TIME");
    }


    public TrafficDayDetailDao(DaoConfig config) {
        super(config);
    }
    
    public TrafficDayDetailDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"TRAFFIC_DAY_DETAIL\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE ," + // 0: id
                "\"START_TIME\" INTEGER NOT NULL ," + // 1: startTime
                "\"PACKAGE_NAME\" TEXT," + // 2: packageName
                "\"RX\" INTEGER NOT NULL ," + // 3: rx
                "\"TX\" INTEGER NOT NULL ," + // 4: tx
                "\"TOTAL\" INTEGER NOT NULL ," + // 5: total
                "\"LAST_TIME\" INTEGER NOT NULL );"); // 6: lastTime
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"TRAFFIC_DAY_DETAIL\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, TrafficDayDetail entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getStartTime());
 
        String packageName = entity.getPackageName();
        if (packageName != null) {
            stmt.bindString(3, packageName);
        }
        stmt.bindLong(4, entity.getRx());
        stmt.bindLong(5, entity.getTx());
        stmt.bindLong(6, entity.getTotal());
        stmt.bindLong(7, entity.getLastTime());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, TrafficDayDetail entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getStartTime());
 
        String packageName = entity.getPackageName();
        if (packageName != null) {
            stmt.bindString(3, packageName);
        }
        stmt.bindLong(4, entity.getRx());
        stmt.bindLong(5, entity.getTx());
        stmt.bindLong(6, entity.getTotal());
        stmt.bindLong(7, entity.getLastTime());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public TrafficDayDetail readEntity(Cursor cursor, int offset) {
        TrafficDayDetail entity = new TrafficDayDetail( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getLong(offset + 1), // startTime
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // packageName
            cursor.getLong(offset + 3), // rx
            cursor.getLong(offset + 4), // tx
            cursor.getLong(offset + 5), // total
            cursor.getLong(offset + 6) // lastTime
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, TrafficDayDetail entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setStartTime(cursor.getLong(offset + 1));
        entity.setPackageName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setRx(cursor.getLong(offset + 3));
        entity.setTx(cursor.getLong(offset + 4));
        entity.setTotal(cursor.getLong(offset + 5));
        entity.setLastTime(cursor.getLong(offset + 6));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(TrafficDayDetail entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(TrafficDayDetail entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(TrafficDayDetail entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
