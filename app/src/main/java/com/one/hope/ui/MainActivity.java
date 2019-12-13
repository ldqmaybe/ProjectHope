package com.one.hope.ui;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.one.hope.R;
import com.one.hope.util.Navigator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private RecyclerView rv;
    private BaseQuickAdapter<String, BaseViewHolder> adapter;
    private List<String> stringList;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_main);
        initData();
        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_main_list, stringList) {

            @Override
            protected void convert(BaseViewHolder helper, String s) {
                helper.setText(R.id.tv, s);
            }
        };
        adapter.setOnItemClickListener((adapter, view, position) -> {
            if ("GreenDao".equals(stringList.get(position))) {
                Navigator.getInstance().navigate2UserList(MainActivity.this);
            } else if ("蓝牙".equals(stringList.get(position))) {
                Navigator.getInstance().navigate2Bluetooth(MainActivity.this);
            }
        });
        rv.setAdapter(adapter);
    }

    private void initData() {
        stringList = new ArrayList<>();
        stringList.add("GreenDao");
        stringList.add("蓝牙");
    }

}
