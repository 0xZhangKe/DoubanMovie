package com.zhangke.doubanmovie.Search;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhangke.doubanmovie.R;
import com.zhangke.doubanmovie.base.BaseRecyclerAdapter;
import com.zhangke.doubanmovie.util.GlideCircleTransform;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ZhangKe on 2018/1/4.
 */

public class SearchResultAdapter extends BaseRecyclerAdapter<SearchResultAdapter.ViewHolder, SearchUserBean>{

    public SearchResultAdapter(Context context, List<SearchUserBean> listData) {
        super(context, listData);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.adapter_search_result, parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SearchUserBean itemBean = listData.get(position);
        holder.tv_name.setText(itemBean.getNickName());
        holder.tv_desc.setText(itemBean.getDescription());

        Glide.with(context)
                .load(itemBean.getUserIcon())
                .transform(new GlideCircleTransform(context))
                .crossFade()
                .into(holder.img_icon);
    }

    class ViewHolder extends BaseRecyclerAdapter.ViewHolder{

        @BindView(R.id.img_icon)
        ImageView img_icon;
        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.tv_desc)
        TextView tv_desc;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
