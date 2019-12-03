package com.one.greendaohope.ui;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.one.greendaohope.R;
import com.one.greendaohope.entity.UserEntity;
import com.one.greendaohope.mvp.contact.UserListContact;
import com.one.greendaohope.mvp.presenter.UserListPresenter;
import com.one.greendaohope.util.Navigator;
import com.one.greendaohope.util.ToastUtils;
import com.one.greendaohope.widget.AddOrUpdateUserDialog;
import com.one.greendaohope.widget.ArticleItemDecortion;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends BaseActivity implements UserListContact.View {

    private static final String TAG = UserListActivity.class.getSimpleName();

    private RecyclerView rv;
    private BaseQuickAdapter<UserEntity, BaseViewHolder> adapter;
    private List<UserEntity> entityList;
    private UserListPresenter presenter;
    private int deletePos;
    private UserEntity addOrUpdateEntity;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_user_list);
        setTopbar();
        presenter = new UserListPresenter(this);
        entityList = new ArrayList<>();
        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new ArticleItemDecortion(this));
        initAdapter();
        presenter.loadAllDatas();
    }

    private void setTopbar() {
        setTopBarTitle("列表");
        showTopBarLeftIcon();
        showTopBarRightIcon(R.mipmap.ic_add, v -> showAddOrUpdateUserDialog(null,true));
    }

    private void showAddOrUpdateUserDialog(UserEntity entity,final boolean isAdd) {
        AddOrUpdateUserDialog.newInstance()
                .isAdd(isAdd)
                .setUserEntity(entity)
                .setConvertListener(userEntity -> {
                    this.addOrUpdateEntity = userEntity;
                    presenter.saveOrUpdateUser(userEntity,isAdd);
                }).show(getSupportFragmentManager());
    }

    private void initAdapter() {
        adapter = new BaseQuickAdapter<UserEntity, BaseViewHolder>(R.layout.item_user_list) {
            @Override
            protected void convert(BaseViewHolder helper, UserEntity userEntity) {
                helper.setText(R.id.tv_name, String.format("姓名：%s", userEntity.getUserName()))
                        .setText(R.id.tv_phone, String.format("联系方式：%s", userEntity.getPhone()))
                        .setText(R.id.tv_address, String.format("地址：%s", userEntity.getAddress()))
                        .addOnClickListener(R.id.btnUpdate)
                        .addOnClickListener(R.id.btnDelete)
                        .addOnClickListener(R.id.ll_content);
            }
        };
        adapter.setOnItemChildClickListener((adapter, view, position) -> {
            ToastUtils.showShortToast("点击位置：" + position);
            int viewId = view.getId();
            if (viewId == R.id.btnDelete) {
                UserListActivity.this.deletePos = position;
                presenter.deleteUser(entityList.get(position).getPhone());
            } else if (viewId == R.id.btnUpdate) {
                showAddOrUpdateUserDialog(entityList.get(position),false);
            }else if (viewId == R.id.ll_content) {
                Navigator.getInstance().navigate2UserDetail(UserListActivity.this, entityList.get(position));
            }
        });
        rv.setAdapter(adapter);
    }


    @Override
    public void loadAllDatasSuccess(List<UserEntity> userEntityList) {
        this.entityList = userEntityList;
        adapter.addData(userEntityList);
    }

    @Override
    public void optionFail(String errMsg) {
        ToastUtils.showShortToast(errMsg);
    }

    @Override
    public void optionSuccess(String sucMsg) {
        ToastUtils.showShortToast(sucMsg);
        entityList.remove(deletePos);
        adapter.remove(deletePos);
    }

    @Override
    public void addSuccess(String sucMsg) {
        ToastUtils.showShortToast(sucMsg);
        entityList.add(addOrUpdateEntity);
        adapter.addData(addOrUpdateEntity);
    }

    @Override
    public void updateSuccess(String sucMsg) {
        ToastUtils.showShortToast(sucMsg);
        adapter.notifyDataSetChanged();
        entityList = adapter.getData();
    }

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, UserListActivity.class);
    }
}
