package com.one.hope.ui;

import android.content.Context;
import android.content.Intent;
import android.widget.EditText;

import com.one.hope.R;
import com.one.hope.entity.UserEntity;
import com.one.hope.mvp.contact.UserDetailContact;
import com.one.hope.util.DBUtils;

public class UserDetailActivity extends BaseActivity implements UserDetailContact.View {
    private EditText etId, etName, etContact, etAddress;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_user_detail);
        setTopBarTitle("详情");
        showTopBarLeftIcon();
        etId = findViewById(R.id.et_id);
        etName = findViewById(R.id.et_name);
        etContact = findViewById(R.id.et_contact);
        etAddress = findViewById(R.id.et_address);
        UserEntity userEntity1 = getIntent().getParcelableExtra("UserEntity");
        UserEntity userEntity = DBUtils.loadUserId(userEntity1.getId());
        etId.setText(String.valueOf(userEntity.getId()));
        etId.setEnabled(false);
        etName.setText(userEntity.getUserName());
        etContact.setText(userEntity.getPhone());
        etAddress.setText(userEntity.getAddress());
    }

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, UserDetailActivity.class);
    }

    @Override
    public void updateSuccess(UserEntity userEntity) {

    }

    @Override
    public void updateFail(String errMsg) {

    }
}
