package com.zhangke.doubanmovie;

/**
 * Created by ZhangKe on 2018/1/3.
 */

public class DoubanMovieBean {

    private String movieName;
    private String movieImageUrl;

    public DoubanMovieBean() {
    }

    public DoubanMovieBean(String movieName, String movieImageUrl) {
        this.movieName = movieName;
        this.movieImageUrl = movieImageUrl;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMovieImageUrl() {
        return movieImageUrl;
    }

    public void setMovieImageUrl(String movieImageUrl) {
        this.movieImageUrl = movieImageUrl;
    }
}
