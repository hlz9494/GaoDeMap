package com.hlz.gaodemap.utill;

/**
 * Created by hlz on 2017-09-12.
 */

public interface CallBack<T> {
    void onSuccess(T t);
    void onFail(String msg);
}
