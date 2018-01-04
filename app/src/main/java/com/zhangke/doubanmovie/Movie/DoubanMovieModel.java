package com.zhangke.doubanmovie.Movie;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.zhangke.doubanmovie.MovieApplication;
import com.zhangke.doubanmovie.util.HttpUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ZhangKe on 2018/1/3.
 */

public class DoubanMovieModel implements IDoubanMovieContract.Model {

    private static final String TAG = "DoubanMovieModel";

    private Context mContext;
    private String peopleId = "127344843";
    private int start = 0;
    private List<DoubanMovieBean> listData = new ArrayList<>();

    private IDoubanMovieContract.View movieView;

    public DoubanMovieModel(Context context, IDoubanMovieContract.View view) {
        mContext = context;
        movieView = view;
    }

    @Override
    public void performRequest() {
        new Thread(() -> getHtmlAndUrl()).start();
    }

    private void getHtmlAndUrl() {
        String url = String.format("https://movie.douban.com/people/%s/collect?start=%s&sort=time&rating=all&filter=all&mode=grid", peopleId, start);
        start += 15;
        String response = getRequest(url);
        if (TextUtils.isEmpty(response)) {
            return;
        }

        final Pattern movieModelPattern = Pattern.compile("<img alt=(.{1,50}) src=(.{30,100}) class=\"\">");
        Matcher movieModelMatcher = movieModelPattern.matcher(response);
        Pattern imgUrlPattern = Pattern.compile("https://img(.{0,5}).doubanio.com/view/photo/s_ratio_poster/public/(.{1,30}).jpg");
        Pattern namePattern = Pattern.compile("alt=\"(.{1,20})\" ");
        boolean isEmpty = true;
        while (movieModelMatcher.find()) {
            isEmpty = false;
            String movieName = "";
            String fileUrl = "";

            String modelString = movieModelMatcher.group();
            Matcher nameMatcher = namePattern.matcher(modelString);
            while (nameMatcher.find()) {
                String name = nameMatcher.group();
                if (name != null && !name.isEmpty()) {
                    name = name.trim();
                    name = name.replaceAll(" ", "_");
                    name = name.replaceAll("\"", "");
                    name = name.replaceAll("alt=", "");
                    Log.i(TAG, "movie name: " + name);
                    movieName = name;
                }
            }
            Matcher urlMatcher = imgUrlPattern.matcher(modelString);
            while (urlMatcher.find()) {
                fileUrl = urlMatcher.group();
                System.out.println(fileUrl);
            }
            if (!TextUtils.isEmpty(movieName) && !TextUtils.isEmpty(fileUrl))
                listData.add(new DoubanMovieBean(movieName, fileUrl));
        }

        if (isEmpty) {
            movieView.onComplete(listData);
        } else {
            performRequest();
        }
    }

    public void reset() {
        start = 0;
        listData.clear();
    }

    @Override
    public void setPeopleId(String peopleId) {
        this.peopleId = peopleId;
    }

    public String getRequest(String requestUrl) {
        String response = "";
        try {
            URL uri = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Charsert", "UTF-8");
            conn.setRequestProperty("Content-Type", "text/html; charset=utf-8");
            final int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                InputStream in = conn.getInputStream();
                InputStreamReader isReader = new InputStreamReader(in);
                BufferedReader bufReader = new BufferedReader(isReader);
                String line;
                StringBuilder sbResponse = new StringBuilder();
                while ((line = bufReader.readLine()) != null) {
                    sbResponse.append(line);
                }
                response = sbResponse.toString();
            } else {
                System.out.println("internet error");
                movieView.onError("internet error:" + responseCode);
            }
        } catch (IOException e) {
            Log.e(TAG, "getRequest: ", e);
            movieView.onError("internet error:" + e.getMessage());
        }
        return response;
    }
}
