package com.one.greendaohope.ui;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.one.greendaohope.R;
import com.one.greendaohope.entity.UserEntity;
import com.one.greendaohope.util.DBUtils;
import com.one.greendaohope.util.ToastUtils;

public class AddActivity extends BaseActivity implements View.OnClickListener {
    private EditText etName, etPhone, etAddress;


    @Override
    protected void initView() {
        setContentView(R.layout.activity_add);
        setTopbar();
        etName = findViewById(R.id.et_name);
        etPhone = findViewById(R.id.et_phone);
        etAddress = findViewById(R.id.et_address);
        Button btnAdd = findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(this);
    }

    private void setTopbar() {
        setTopBarTitle("添加");
        showTopBarLeftIcon();
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId) {
            case R.id.btn_add:
                if (TextUtils.isEmpty(etName.getText().toString())) {
                    ToastUtils.showShortToast("请输入姓名");
                    return;
                }
                if (TextUtils.isEmpty(etPhone.getText().toString())) {
                    ToastUtils.showShortToast("请输入联系方式");
                    return;
                }
                if (TextUtils.isEmpty(etAddress.getText().toString())) {
                    ToastUtils.showShortToast("请输入联系地址");
                    return;
                }
                UserEntity user = new UserEntity();
                user.setPhone(etName.getText().toString());
                user.setUserName(etPhone.getText().toString());
                user.setAddress(etAddress.getText().toString());
                ToastUtils.showShortToast(DBUtils.insert(user) ? "添加成功" : "添加失败");
                break;
            default:
        }
    }

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, AddActivity.class);
    }
}
