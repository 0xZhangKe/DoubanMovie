package com.zhangke.doubanmovie.Movie;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.zhangke.doubanmovie.R;
import com.zhangke.doubanmovie.base.BaseAppCompatActivity;
import com.zhangke.doubanmovie.util.PermissionUtil;
import com.zhangke.doubanmovie.util.UiUtil;
import com.zhangke.doubanmovie.widget.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseAppCompatActivity implements IDoubanMovieContract.View{

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private List<DoubanMovieBean> listData = new ArrayList<>();
    private DoubanMovieAdapter adapter;

    private IDoubanMovieContract.Model doubanMovieModel;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        UiUtil.setWindow(this);
        ButterKnife.bind(this);

        List<String> permissionList = new ArrayList<>();
        for (int i = 0; i < PermissionUtil.PERMISSION.length; i++) {
            if (PermissionUtil.isLacksOfPermission(MainActivity.this, PermissionUtil.PERMISSION[i])) {
                permissionList.add(PermissionUtil.PERMISSION[i]);
            }
        }
        String[] permission = permissionList.toArray(new String[permissionList.size()]);
        if (permission.length > 0) {
            ActivityCompat.requestPermissions(MainActivity.this, permission, 0x12);
        }else{
            initData();
        }
    }

    private void initData(){
        adapter = new DoubanMovieAdapter(this, listData);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(5,StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SpacesItemDecoration());

        doubanMovieModel = new DoubanMovieModel(this, this);
        doubanMovieModel.reset();
        doubanMovieModel.performRequest();
        showRoundProgressDialog();
    }

    @Override
    public void onError(String errorMsg) {
        showToastMessage(errorMsg);
        closeRoundProgressDialog();
    }

    @Override
    public void onComplete(List<DoubanMovieBean> data) {
        listData.clear();
        listData.addAll(data);
        runOnUiThread(() ->{
            adapter.notifyDataSetChanged();
            closeRoundProgressDialog();
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 0x12 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            initData();
        }
    }
}
