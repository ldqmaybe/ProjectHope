package com.one.hope.mvp.presenter;

import com.one.hope.entity.UserEntity;
import com.one.hope.mvp.contact.UserListContact;
import com.one.hope.ui.UserListActivity;
import com.one.hope.util.DBUtils;

import java.util.List;

/**
 * @author LinDingQiang
 * @time 2019/8/22 15:18
 * @email dingqiang.l@verifone.cn
 */
public class UserListPresenter implements UserListContact.Presenter {
    private UserListActivity context;

    public UserListPresenter(UserListActivity context) {
        this.context = context;
    }

    @Override
    public void loadAllDatas() {
        List<UserEntity> entityList = DBUtils.loadAllUsers();
        if (entityList == null || entityList.size() == 0) {
            context.optionFail("获取数据为空");
            return;
        }
        context.loadAllDatasSuccess(entityList);
    }

    @Override
    public void deleteUser(String phoneNo) {
        UserEntity entity = DBUtils.loadUserByPhoneNo(phoneNo);
        if (entity == null) {
            context.optionFail("该用户不存在");
            return;
        }
        DBUtils.deleteUser(entity);
        context.optionSuccess("删除成功");
    }

    @Override
    public void saveOrUpdateUser(UserEntity userEntity, boolean isAdd) {
        if (isAdd) {
            UserEntity dbEntity = DBUtils.loadUserByPhoneNo(userEntity.getPhone());
            if (dbEntity != null) {
                context.optionFail("用户已存在");
                return;
            }
            DBUtils.saveUser(userEntity);
            context.addSuccess("添加成功");
        } else {
            DBUtils.updateUser(userEntity);
            context.updateSuccess("修改成功");
        }
    }

}
