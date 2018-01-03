package com.zhangke.doubanmovie;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zhangke.doubanmovie.base.BaseRecyclerAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ZhangKe on 2018/1/3.
 */

public class DoubanMovieAdapter extends BaseRecyclerAdapter<DoubanMovieAdapter.ViewHolder, DoubanMovieBean> {

    public DoubanMovieAdapter(Context context, List<DoubanMovieBean> listData) {
        super(context, listData);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    public class ViewHolder extends BaseRecyclerAdapter.ViewHolder{

        @BindView(R.id.img_view)
        ImageView imgView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
