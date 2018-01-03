package com.zhangke.doubanmovie;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.zhangke.doubanmovie.base.BaseAppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseAppCompatActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);

    }
}
