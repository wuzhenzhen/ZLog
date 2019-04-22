package com.github.library.flow.entity;

import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

/**
 * Created by wzz on 2019/04/22.
 * wuzhenzhen@tiamaes.com
 * 流量明细
 */
public class TrafficDetail {
    @Id(autoincrement = true)
    @Unique
    private Long id;

    private long startTime;  //开机时间
    private String packageName;  //包名
    private long rx;    //接收数据包
    private long tx;    //发送数据包
    private long total; //总数据包
}
