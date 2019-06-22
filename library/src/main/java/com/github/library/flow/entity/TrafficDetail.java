package com.github.library.flow.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by wzz on 2019/04/22.
 * wuzhenzhen@tiamaes.com
 * 流量 明细数据
 */
@Entity
public class TrafficDetail {
    @Id(autoincrement = true)
    @Unique
    private Long id;

    private String packageName;  //包名
    private long startTime;      //开机时间
    private int uid;    //进程id
    private long uidRX;
    private long uidTX;
    private long mobileRX;    //接收数据包
    private long mobileTX;    //发送数据包
    private long totalRX;    //接收数据包
    private long totalTX;    //发送数据包
    private long total; //总数据包
    private long lastTime; //最后一次更新时间， 也可以是插入时间

    @Generated(hash = 433901232)
    public TrafficDetail(Long id, String packageName, long startTime, int uid,
                         long uidRX, long uidTX, long mobileRX, long mobileTX, long totalRX,
                         long totalTX, long total, long lastTime) {
        this.id = id;
        this.packageName = packageName;
        this.startTime = startTime;
        this.uid = uid;
        this.uidRX = uidRX;
        this.uidTX = uidTX;
        this.mobileRX = mobileRX;
        this.mobileTX = mobileTX;
        this.totalRX = totalRX;
        this.totalTX = totalTX;
        this.total = total;
        this.lastTime = lastTime;
    }

    @Generated(hash = 1539557416)
    public TrafficDetail() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public long getUidRX() {
        return uidRX;
    }

    public void setUidRX(long uidRX) {
        this.uidRX = uidRX;
    }

    public long getUidTX() {
        return uidTX;
    }

    public void setUidTX(long uidTX) {
        this.uidTX = uidTX;
    }

    public long getMobileRX() {
        return mobileRX;
    }

    public void setMobileRX(long mobileRX) {
        this.mobileRX = mobileRX;
    }

    public long getMobileTX() {
        return mobileTX;
    }

    public void setMobileTX(long mobileTX) {
        this.mobileTX = mobileTX;
    }

    public long getTotalRX() {
        return totalRX;
    }

    public void setTotalRX(long totalRX) {
        this.totalRX = totalRX;
    }

    public long getTotalTX() {
        return totalTX;
    }

    public void setTotalTX(long totalTX) {
        this.totalTX = totalTX;
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
        return "TrafficDetail{" +
                "id=" + id +
                ", packageName='" + packageName + '\'' +
                ", startTime=" + startTime +
                ", uid=" + uid +
                ", uidRX=" + uidRX +
                ", uidTX=" + uidTX +
                ", mobileRX=" + mobileRX +
                ", mobileTX=" + mobileTX +
                ", totalRX=" + totalRX +
                ", totalTX=" + totalTX +
                ", total=" + total +
                ", lastTime=" + lastTime +
                '}';
    }
}
