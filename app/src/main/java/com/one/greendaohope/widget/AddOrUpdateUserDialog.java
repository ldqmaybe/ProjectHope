package com.one.greendaohope.widget;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.one.greendaohope.R;
import com.one.greendaohope.entity.UserEntity;

import org.greenrobot.greendao.annotation.NotNull;

/**
 * 修改云服务平台ip
 *
 * @author LinDingQiang
 * @time 2019/4/3 12:17
 * @email dingqiang.l@verifone.cn
 */
public class AddOrUpdateUserDialog extends BaseNiceDialog {
    //管理员密码
    private UserEntity userEntity;
    private boolean isAdd;
    private CallBack mListener;

    public static AddOrUpdateUserDialog newInstance() {
        Bundle bundle = new Bundle();
        AddOrUpdateUserDialog dialog = new AddOrUpdateUserDialog();
        dialog.setWidth(300)
                .setOutCancel(false)
                .setDimAmount(0.3f)
                .setArguments(bundle);
        return dialog;
    }

    public AddOrUpdateUserDialog isAdd(boolean isAdd) {
        this.isAdd = isAdd;
        return this;
    }

    public AddOrUpdateUserDialog setUserEntity(@NotNull UserEntity userEntity) {
        this.userEntity = userEntity;
        return this;
    }


    @Override
    public int intLayoutId() {
        return R.layout.dialog_add_or_update_user;
    }

    @Override
    public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
        LinearLayout llContent = holder.getView(R.id.ll_content);
        TextView tvId = holder.getView(R.id.tv_id);
        final EditText etName = holder.getView(R.id.et_name);
        final EditText etContact = holder.getView(R.id.et_contact);
        final EditText etAdress = holder.getView(R.id.et_address);
        llContent.setVisibility(isAdd ? View.GONE : View.VISIBLE);
        if (!isAdd) {
            tvId.setText(String.valueOf(userEntity.getId()));
            etName.setText(userEntity.getUserName());
            etContact.setText(userEntity.getPhone());
            etAdress.setText(userEntity.getAddress());
        }
        holder.setOnClickListener(R.id.bt_cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        holder.setOnClickListener(R.id.bt_confirm, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userEntity == null) {
                    userEntity = new UserEntity();
                }
                userEntity.setAddress(etAdress.getText().toString());
                userEntity.setUserName(etName.getText().toString());
                userEntity.setPhone(etContact.getText().toString());
                mListener.onComfirm(userEntity);
                dialog.dismiss();
            }
        });
    }

    public AddOrUpdateUserDialog setConvertListener(CallBack listener) {
        this.mListener = listener;
        return this;
    }

    public interface CallBack {
        void onComfirm(UserEntity userEntity);
    }
}
