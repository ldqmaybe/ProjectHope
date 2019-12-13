package com.one.hope.mvp.contact;

import com.one.hope.entity.UserEntity;

import java.util.List;

/**
 * @author LinDingQiang
 * @time 2019/8/22 17:36
 * @email dingqiang.l@verifone.cn
 */
public interface UserListContact {
    public interface View {
       void loadAllDatasSuccess(List<UserEntity> userEntityList);
       void optionFail(String errMsg);
       void optionSuccess(String sucMsg);
       void addSuccess(String sucMsg);
       void updateSuccess(String sucMsg);
    }
    public interface Presenter{
        void loadAllDatas();
        void deleteUser(String phoneNo);
        void saveOrUpdateUser(UserEntity userEntity, boolean isAdd);
    }
}
