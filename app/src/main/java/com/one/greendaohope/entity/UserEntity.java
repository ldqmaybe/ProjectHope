package com.one.greendaohope.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * @author LinDingQiang
 * @time 2019/8/19 14:26
 * @email dingqiang.l@verifone.cn
 */
@Entity(nameInDb = "USER")
public class UserEntity {
    @Id(autoincrement = true)
    private Long id;
    private String userName;
    private String phone;
    private String address;


    @Generated(hash = 638665508)
    public UserEntity(Long id, String userName, String phone, String address) {
        this.id = id;
        this.userName = userName;
        this.phone = phone;
        this.address = address;
    }
    @Generated(hash = 1433178141)
    public UserEntity() {
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "userName='" + userName + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
