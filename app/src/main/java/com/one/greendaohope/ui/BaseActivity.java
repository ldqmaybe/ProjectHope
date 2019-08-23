package com.one.greendaohope.ui;


import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.one.greendaohope.R;
import com.one.greendaohope.util.AppManager;
import com.one.greendaohope.util.HideUtil;
import com.one.greendaohope.util.StatusBarUtil;


/**
 * Activity的基类
 */
public abstract class BaseActivity extends AppCompatActivity {
    public Context mContext;
    private ProgressDialog dialog;
    private LinearLayout linearLayout;
    private ImageView ivLeft;
    private TextView tvTitle;
    private ImageView ivRight;
    private boolean showTopBar = true;
    private boolean showTintBar = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getInstance().addActivityTOStack(this);
        showTopBar = isShowTopBarTag();
        showTintBar = isShowTintBarTag();
        StatusBarUtil.setTransparent(this);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary));
        mContext = this;
        HideUtil.init(this);

        this.initView();
        //强制在基类Intent判空
        if (null != getIntent()) {
            handleIntent(getIntent());
        }
    }


    @Override
    public void setContentView(int layoutResID) {
        if (showTopBar) {
            initContentView();
            LayoutInflater.from(this).inflate(layoutResID, linearLayout, true);
        } else {
            super.setContentView(layoutResID);
        }
    }

    @Override
    public void setContentView(View view) {
        if (showTopBar) {
            initContentView();
            linearLayout.addView(view);
        } else {
            super.setContentView(view);
        }
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        if (showTopBar) {
            initContentView();
            linearLayout.addView(view, params);
        } else {
            super.setContentView(view, params);
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void initContentView() {

        if (linearLayout == null) {
            linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            LayoutInflater.from(this).inflate(R.layout.toolbar, linearLayout, true);
            ViewGroup viewGroup = findView(android.R.id.content);
            viewGroup.removeAllViews();
            linearLayout.setFitsSystemWindows(true);
            linearLayout.setClipToPadding(true);
            viewGroup.addView(linearLayout);
            initTopBar();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getInstance().finishActivity(this);
    }
    private boolean isShowTopBarTag() {
        return true;
    }

    public boolean isShowTintBarTag() {
        return true;
    }

    private void initTopBar() {
        if (linearLayout != null) {
            ivLeft = linearLayout.findViewById(R.id.topbar_left);
            ivLeft.setVisibility(View.GONE);

            tvTitle = linearLayout.findViewById(R.id.topbar_title);
            tvTitle.setVisibility(View.GONE);

            ivRight = linearLayout.findViewById(R.id.topbar_right);
            ivRight.setVisibility(View.GONE);
        }
    }
    protected void setTopBarTitle(int resid) {
        setTopBarTitle(getString(resid));
    }
    protected void setTopBarTitle(String title) {
        if (tvTitle != null && title != null) {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(title);
        }
    }
    protected void showTopBarLeftIcon(int resId, View.OnClickListener listener) {
        if (ivLeft != null) {
            ivLeft.setVisibility(View.VISIBLE);
            ivLeft.setImageResource(resId);
            ivLeft.setOnClickListener(listener);
        }
    }
    protected void showTopBarLeftIcon() {
        if (ivLeft != null) {
            ivLeft.setVisibility(View.VISIBLE);
            ivLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }
    protected void showTopBarLeftIcon(View.OnClickListener listener) {
        if (ivLeft != null) {
            ivLeft.setVisibility(View.VISIBLE);
            ivLeft.setOnClickListener(listener);
        }
    }
    protected void showTopBarRightIcon(int resId, View.OnClickListener listener) {
        if (ivRight != null) {
            ivRight.setVisibility(View.VISIBLE);
            ivRight.setImageResource(resId);
            ivRight.setOnClickListener(listener);
        }
    }

    protected void showTopBarRightIcon(int resId) {
        if (ivRight != null) {
            ivRight.setVisibility(View.VISIBLE);
            ivRight.setImageResource(resId);
        }
    }

    /**
     * 显示加载
     *
     * @param text 加载框中的内容
     */
    public void showDialog(String text) {
        dialog = new ProgressDialog(this);
        dialog.setMessage(text);
        dialog.show();
    }

    /**
     * 隐藏加载
     */
    public void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            // progressDialog.hide();会导致android.view.WindowLeaked
            dialog.dismiss();
        }
    }

    /**
     * 初始化页面
     */
    protected abstract void initView();

    /**
     * 封装的findViewByID方法
     */
    @SuppressWarnings("unchecked")
    protected <T extends View> T findView(@IdRes int id) {
        return (T) super.findViewById(id);
    }

    /**
     * 处理Intent，防止开发人员没做Intent判空
     */
    protected void handleIntent(Intent intent) {
    }

}
