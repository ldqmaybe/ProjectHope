package com.one.greendaohope.ui;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.one.greendaohope.R;
import com.one.greendaohope.entity.UserEntity;
import com.one.greendaohope.mvp.contact.UserListContact;
import com.one.greendaohope.mvp.presenter.UserListPresenter;
import com.one.greendaohope.util.Navigator;
import com.one.greendaohope.util.ToastUtils;
import com.one.greendaohope.widget.ArticleItemDecortion;

import java.util.List;

public class UserListActivity extends BaseActivity implements UserListContact.View {

    private static final String TAG = UserListActivity.class.getSimpleName();

    private RecyclerView rv;
    private BaseQuickAdapter<UserEntity, BaseViewHolder> adapter;
    private List<UserEntity> entityList;
    private UserListPresenter presenter;
    private int deletePos;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_user_list);
        setTopbar();
        presenter = new UserListPresenter(this);
        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new ArticleItemDecortion(this));
        initAdapter();
        presenter.loadAllDatas();
    }

    private void setTopbar() {
        setTopBarTitle("列表");
        showTopBarLeftIcon();
    }

    private void initAdapter() {
        adapter = new BaseQuickAdapter<UserEntity, BaseViewHolder>(R.layout.item_user_list) {
            @Override
            protected void convert(BaseViewHolder helper, UserEntity userEntity) {
                helper.setText(R.id.tv_name, userEntity.getUserName())
                        .setText(R.id.tv_phone, userEntity.getPhone())
                        .setText(R.id.tv_address, String.format("地址：%s", userEntity.getAddress()))
                        .addOnClickListener(R.id.btnDelete)
                        .addOnClickListener(R.id.ll_content);
            }
        };
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                ToastUtils.showShortToast("点击位置：" + position);
                int viewId = view.getId();
                if (viewId == R.id.btnDelete) {
                    UserListActivity.this.deletePos = position;
                    presenter.deleteUser(entityList.get(position).getPhone());
                } else if (viewId == R.id.ll_content) {
                    Navigator.getInstance().navigate2UserDetail(UserListActivity.this);
                }
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

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, UserListActivity.class);
    }
}
