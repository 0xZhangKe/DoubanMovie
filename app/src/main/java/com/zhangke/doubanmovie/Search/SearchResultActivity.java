package com.zhangke.doubanmovie.Search;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.zhangke.doubanmovie.Movie.MovieActivity;
import com.zhangke.doubanmovie.MovieApplication;
import com.zhangke.doubanmovie.R;
import com.zhangke.doubanmovie.base.BaseAppCompatActivity;
import com.zhangke.doubanmovie.base.BaseRecyclerAdapter;
import com.zhangke.doubanmovie.util.HttpUtil;
import com.zhangke.doubanmovie.widget.PullToRefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ZhangKe on 2018/1/4.
 */

public class SearchResultActivity extends BaseAppCompatActivity {

    @BindView(R.id.coordinator)
    CoordinatorLayout coordinator;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view)
    PullToRefreshRecyclerView recyclerView;
    @BindView(R.id.floating_btn)
    FloatingActionButton floating_btn;

    private List<SearchUserBean> listData = new ArrayList<>();
    private SearchResultAdapter adapter;

    private String nickName;

    private int start = 0;

    private Dialog mInputPeopleIdDialog;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_search_result;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);

        nickName = getIntent().getStringExtra(INTENT_ARG_01);
        initToolbar(toolbar, nickName, true);

        adapter = new SearchResultAdapter(this, listData);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setOnPullToBottomListener(() -> {
            performRequest(false);
        });

        adapter.setOnItemClickListener((View view, int position) -> {
            Intent intent = new Intent(this, MovieActivity.class);
            intent.putExtra(INTENT_ARG_01, listData.get(position).getUserId() + "");
            startActivity(intent);
        });

        performRequest(true);
    }

    private void performRequest(boolean firstPage){
        if(firstPage){
            listData.clear();
            start = 0;
            showRoundProgressDialog();
        }
        String url = String.format("https://www.douban.com/j/search?q=%s&start=%s&cat=1005", nickName, start);
        HttpUtil.simpleGetRequest(MovieApplication.getInstance().getRequestQueue(),
                url,
                new HttpUtil.OnStringDataCallbackListener() {
                    @Override
                    public void onSuccess(String data) {
                        if(firstPage){
                            closeRoundProgressDialog();
                        }else{
                            recyclerView.setLoading(false);
                        }
                        SearchResultBean resultBean = JSON.parseObject(data, new TypeReference<SearchResultBean>() {
                        });
                        List<String> userList = resultBean.getItems();
                        if(userList != null && !userList.isEmpty()){
                            for(String s : userList){
                                listData.add(new SearchUserBean(s));
                            }
                            start += 20;
                        }
                        adapter.notifyDataSetChanged();
                        Log.e(TAG, "onSuccess: " + data);
                    }

                    @Override
                    public void onError(String error) {
                        if(firstPage) {
                            closeRoundProgressDialog();
                        }else{
                            recyclerView.setLoading(false);
                        }
                        showSnackbar(coordinator, error);
                    }
                });
    }

    @OnClick(R.id.floating_btn)
    public void onFloatingClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_input_people_id, null, false);
        EditText etId = (EditText)dialogView.findViewById(R.id.et_people_id);
        dialogView.findViewById(R.id.tv_go).setOnClickListener((View v) -> {
            mInputPeopleIdDialog.dismiss();
            String id = etId.getText().toString();
            if(TextUtils.isEmpty(id)){
                showSnackbar(coordinator, "请输入豆瓣 ID");
                return;
            }
            Intent intent = new Intent(this, MovieActivity.class);
            intent.putExtra(INTENT_ARG_01, id);
            startActivity(intent);
        });
        builder.setView(dialogView);
        mInputPeopleIdDialog = builder.create();
        mInputPeopleIdDialog.show();
    }
}