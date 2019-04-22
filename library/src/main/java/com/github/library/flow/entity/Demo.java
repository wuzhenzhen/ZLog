package com.github.library.flow.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class Demo {
    @Id
    Long id;
    String name;

    @Generated(hash = 933678388)
    public Demo(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Generated(hash = 571290164)
    public Demo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
