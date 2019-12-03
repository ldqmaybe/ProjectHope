package com.one.greendaohope.mvp.contact;

import com.one.greendaohope.entity.UserEntity;

/**
 * @author LinDingQiang
 * @time 2019/12/2 15:12
 * @email dingqiang.l@verifone.cn
 */
public interface UserDetailContact {
    public interface View {
        void updateSuccess(UserEntity userEntity);
        void updateFail(String errMsg);
    }
    public interface Presenter{
        void updateUser(UserEntity userEntity);
    }
}
