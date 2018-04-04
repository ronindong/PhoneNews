package com.ronin.net.api;

import com.ronin.net.dao.MovieService;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import test.bean.Movie;

/**
 * Created by donghailong on 2018/4/4.
 */

public class ApiMethods {
    /**
     * 封装线程管理和订阅的过程
     */
    public static void apiSubscribe(Observable<?> observable, Observer observer) {
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 用于获取豆瓣电影Top250的数据
     *
     * @param observer 由调用者传过来的观察者对象
     * @param start    起始位置
     * @param count    获取长度
     */
    public static void getTopMovie(Observer<Movie> observer, int start, int count) {
        apiSubscribe(Net.getService(MovieService.class).getTopMovie(start, count), observer);
    }
}