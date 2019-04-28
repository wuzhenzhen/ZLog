package com.github.library.flow.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by wzz on 2019/04/23.
 * wuzhenzhen@tiamaes.com
 *
 *
 */
@Entity
public class TrafficDayDetail {
    @Id(autoincrement = true)
    @Unique
    private Long id;

    private long startTime;
    private String packageName;
    private long rx;
    private long tx;
    private long total;
    private long lastTime;

    @Generated(hash = 1620998934)
    public TrafficDayDetail(Long id, long startTime, String packageName, long rx,
            long tx, long total, long lastTime) {
        this.id = id;
        this.startTime = startTime;
        this.packageName = packageName;
        this.rx = rx;
        this.tx = tx;
        this.total = total;
        this.lastTime = lastTime;
    }

    @Generated(hash = 1501637570)
    public TrafficDayDetail() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public long getRx() {
        return rx;
    }

    public void setRx(long rx) {
        this.rx = rx;
    }

    public long getTx() {
        return tx;
    }

    public void setTx(long tx) {
        this.tx = tx;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

    @Override
    public String toString() {
        return "TrafficDayDetail{" +
                "id=" + id +
                ", startTime=" + startTime +
                ", packageName='" + packageName + '\'' +
                ", rx=" + rx +
                ", tx=" + tx +
                ", total=" + total +
                ", lastTime=" + lastTime +
                '}';
    }
}
