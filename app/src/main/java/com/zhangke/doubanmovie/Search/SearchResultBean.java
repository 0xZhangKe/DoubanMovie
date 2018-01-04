package com.zhangke.doubanmovie.Search;

import java.util.List;

/**
 * Created by ZhangKe on 2018/1/4.
 */

public class SearchResultBean {

    private int total;
    private int limit;
    private boolean more;
    private List<String> items;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public boolean isMore() {
        return more;
    }

    public void setMore(boolean more) {
        this.more = more;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }
}
