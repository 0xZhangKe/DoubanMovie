package com.zhangke.doubanmovie.Search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.EditText;

import com.zhangke.doubanmovie.Movie.MainActivity;
import com.zhangke.doubanmovie.R;
import com.zhangke.doubanmovie.base.BaseAppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ZhangKe on 2018/1/3.
 */

public class SearchUserActivity extends BaseAppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_name)
    EditText etName;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_search_user;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);

        initToolbar(toolbar, "NickName", false);
    }

    @OnClick(R.id.btn_search)
    public void onSearchClick(){
        String nickName = etName.getText().toString();
        if(TextUtils.isEmpty(nickName)){
            showToastMessage("请输入昵称");
            return;
        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
