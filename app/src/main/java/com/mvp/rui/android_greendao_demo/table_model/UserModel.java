package com.mvp.rui.android_greendao_demo.table_model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by 0200030 on 2018/3/20.
 */
@Entity
public class UserModel {
    public static final long serialVersionUID = 0x11006;
    @Id
    private Long id;

    private String name;

    private int age;


    @Generated(hash = 1942278017)
    public UserModel(Long id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    @Generated(hash = 782181818)
    public UserModel() {
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
