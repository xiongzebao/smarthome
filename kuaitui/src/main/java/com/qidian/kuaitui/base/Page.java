package com.qidian.kuaitui.base;

/**
 * @author xiongbin
 * @description:
 * @date : 2020/12/29 17:21
 */

public class Page {
    public int pageIndex=1;
    public int size=10;

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Page(){

    }

    public Page(int pageIndex, int size) {
        this.pageIndex = pageIndex;
        this.size = size;
    }

    public static Page getPage(){
       return new Page();
    }

}
