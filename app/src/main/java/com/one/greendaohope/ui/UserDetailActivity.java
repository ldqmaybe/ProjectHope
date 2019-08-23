package com.one.greendaohope.ui;

import android.content.Context;
import android.content.Intent;

import com.one.greendaohope.R;

public class UserDetailActivity extends BaseActivity {

    @Override
    protected void initView() {
        setContentView(R.layout.activity_user_detail);
        setTopBarTitle("详情");
        showTopBarLeftIcon();
    }
    public static Intent getCallingIntent(Context context) {
        return new Intent(context, UserDetailActivity.class);
    }
}
