package com.erongdu.wireless.network.entity;

import java.io.Serializable;

/**
 * Author: TinhoXu
 * E-mail: xth@erongdu.com
 * Date: 2016/5/11 10:41
 * <p/>
 * Description: 页数信息
 */
@SuppressWarnings("unused")
public class PageMo implements Serializable {
    /** 总页数 */
    //    @SerializedName(value = "totalPage", alternate = {"totalPage"})
    private int pages;
    /** 当前页数 - 默认第一页 */
    //    @SerializedName(value = "current", alternate = {"current"})
    private int current  = 1;
    /** 每次加载数量 - 默认10条 */
    //    @SerializedName(value = "pageSize", alternate = {"pageSize"})
    private int pageSize = 15;
    /** 总共条数 */
    //    @SerializedName(value = "total", alternate = {"total"})
    private int total;



    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    /**
     * 是否是刷新
     */
    public boolean isRefresh() {
        return current == 1;
    }

    /**
     * 下拉刷新
     */
    public void refresh() {
        current = 1;
    }

    /**
     * 上拉加载
     */
    public void loadMore() {
        current++;
    }

    public int getPage() {
        return current;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    /**
     * @return 是否已经显示完全
     * true - 已经是最后页，无需再次请求，loadMore 无需再显示
     * false - 还不是最后页，需要再次请求数据，loadMore 需要再显示
     */
    public boolean isOver() {
        return pages <= current;
    }
}
