package com.example.myapplication.data.callback;

public interface DataCallBack<T> {
    void onEmit(T data);

    void onCompleted();

    void onError(Throwable throwable);
}
