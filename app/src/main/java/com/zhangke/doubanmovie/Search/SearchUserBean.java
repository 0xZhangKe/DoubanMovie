package com.zhangke.doubanmovie.Search;

import android.text.TextUtils;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ZhangKe on 2018/1/4.
 */

public class SearchUserBean {

    private static final String TAG = "SearchUserBean";

    private String userIcon;
    private String nickName;
    private String userId;
    private String description;

    private Pattern userIconPattern = Pattern.compile("https://img(.{15,40}).jpg");
    private Pattern nickNamePattern = Pattern.compile("alt=\"(.{1,20})\"");
    private Pattern userIdPattern = Pattern.compile("sid:(.{3,13}),");
    private Pattern descriptionPattern = Pattern.compile("info\">(.{5,14})<");

    public SearchUserBean() {
    }

    public SearchUserBean(String html) {
        html = html.replaceAll("\n", "");
        html = html.replaceAll(" ", "");

        Matcher matcher = userIconPattern.matcher(html);
        while (matcher.find()) {
            userIcon = matcher.group();
        }

        matcher = nickNamePattern.matcher(html);
        while (matcher.find()) {
            nickName = matcher.group();
            if(!TextUtils.isEmpty(nickName) && nickName.length() > 5){
                try {
                    nickName = nickName.substring(5, nickName.length() - 1);
                }catch(Exception e){
                    Log.e(TAG, "SearchUserBean: ", e);
                }
            }
        }

        matcher = userIdPattern.matcher(html);
        while (matcher.find()) {
            userId = matcher.group();
            if(!TextUtils.isEmpty(userId) && userId.length() > 4){
                try {
                    userId = userId.substring(4, userId.length() - 1);
                }catch(Exception e){
                    Log.e(TAG, "SearchUserBean: ", e);
                }
            }
        }

        matcher = descriptionPattern.matcher(html);
        while (matcher.find()) {
            description = matcher.group();
            if(!TextUtils.isEmpty(description) && description.length() > 6){
                try {
                    description = description.substring(6, description.length() - 1);
                }catch(Exception e){
                    Log.e(TAG, "SearchUserBean: ", e);
                }
            }
        }
    }

    public SearchUserBean(String userIcon, String nickName, String userId, String description) {
        this.userIcon = userIcon;
        this.nickName = nickName;
        this.userId = userId;
        this.description = description;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
