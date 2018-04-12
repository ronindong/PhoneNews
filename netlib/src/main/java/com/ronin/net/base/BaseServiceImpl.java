package com.ronin.net.base;

import com.ronin.net.Net;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * @author donghailong
 */
public abstract class BaseServiceImpl<T> {
    private static final Map<String, BaseServiceImpl> registryMap = new HashMap<>();
    protected T service;
    private Net net;

    protected BaseServiceImpl() {
        if (null != net) {
            net = new Net.Builder()
                    .setBaseUrl(getBaseUrl())
                    .setHeaders(getHeaders())
                    .build();
        }
        service = net.getService(this.serviceClass());
        try {
            String clazzName = this.getClass().getName();
            if (registryMap.containsKey(clazzName)) {
                throw new SingletonException("Cannot construct instance for class "
                        + clazzName + ", since an instance already exists!");
            } else {
                synchronized (registryMap) {
                    if (registryMap.containsKey(clazzName)) {
                        throw new SingletonException("Cannot construct instance for class "
                                + clazzName + ", since an instance already exists!");
                    } else {
                        registryMap.put(clazzName, this);
                    }
                }
            }
        } catch (SingletonException e) {
            e.printStackTrace();
        }

    }



    /**
     * 接口服务类
     *
     * @return
     */
    protected abstract Class<T> serviceClass();

    @NonNull
    protected abstract String getBaseUrl();

    protected Map<String, String> getHeaders() {
        return null;
    }

    public T getService() {
        return service;
    }


    @SuppressWarnings("unchecked")
    public static <S extends BaseServiceImpl> S get(final Class<S> clazz) {
        String clazzName = clazz.getName();
        if (!registryMap.containsKey(clazzName)) {
            synchronized (registryMap) {
                if (!registryMap.containsKey(clazzName)) {
                    S instance = null;
                    try {
                        instance = clazz.newInstance();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    return instance;
                }
            }
        }
        return (S) registryMap.get(clazzName);
    }

    private static class SingletonException extends Exception {
        /**
         *
         */
        private static final long serialVersionUID = -8633183690442262445L;

        private SingletonException(String message) {
            super(message);
        }
    }


}
