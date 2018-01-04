package com.zhangke.doubanmovie;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by ZhangKe on 2018/1/4.
 */

public class MovieApplication extends Application {

    private static final String TAG = "MovieApplication";

    private static MovieApplication application;
    public static Context mContext;

    private RequestQueue mRequestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        mContext = getApplicationContext();
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
    }

    public static MovieApplication getInstance() {
        return application;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request, String tag) {
        request.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(request);
    }

    public <T> void addToRequestQueue(Request<T> request) {
        request.setTag(TAG);
        getRequestQueue().add(request);
    }

    public void cancelPendingRequest(Object tag) {
        getRequestQueue().cancelAll(tag);
    }
}
