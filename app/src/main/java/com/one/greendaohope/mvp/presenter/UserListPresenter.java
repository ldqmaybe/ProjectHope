package com.one.greendaohope.mvp.presenter;

import com.one.greendaohope.entity.UserEntity;
import com.one.greendaohope.mvp.contact.UserListContact;
import com.one.greendaohope.ui.UserListActivity;
import com.one.greendaohope.util.DBUtils;

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
        List<UserEntity> entityList = DBUtils.loadAll(UserEntity.class);
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
        DBUtils.delete(entity);
        context.optionSuccess("删除成功");
    }

}
