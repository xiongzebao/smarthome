package com.erongdu.wireless.network.entity;

import java.util.List;

/**
 * Author: TinhoXu
 * E-mail: xth@erongdu.com
 * Date: 2016/5/11 11:26
 * <p/>
 * Description: 反序列化带page的List数据
 */
@SuppressWarnings("unused")
public class ListData<T> extends PageMo {
    /** list数据 */
    private List<T> list;

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
