package com.one.greendaohope.ui;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.one.greendaohope.R;
import com.one.greendaohope.util.Navigator;

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
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if ("列表".equals(stringList.get(position))) {
                    Navigator.getInstance().navigate2UserList(MainActivity.this);
                }else if ("崩溃".equals(stringList.get(position))) {

                }
            }
        });
        rv.setAdapter(adapter);
    }

    private void initData() {
        stringList = new ArrayList<>();
        stringList.add("添加");
        stringList.add("列表");
        stringList.add("崩溃");
    }

}
