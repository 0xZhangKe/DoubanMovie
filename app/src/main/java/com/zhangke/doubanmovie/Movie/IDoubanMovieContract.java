package com.zhangke.doubanmovie.Movie;

import java.util.List;

/**
 * Created by ZhangKe on 2018/1/3.
 */

public interface IDoubanMovieContract {

    interface View{
        void onError(String errorMsg);
        void onComplete(List<DoubanMovieBean> data);
    }

    interface Model{
        void reset();
        void performRequest();
    }
}
