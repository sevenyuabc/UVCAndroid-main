package com.cicada.kidscard.net.retrofit;


import com.cicada.kidscard.config.AppContext;
import com.cicada.kidscard.net.BaseURL;
import com.cicada.kidscard.net.exception.BusinessException;
import com.cicada.kidscard.utils.NetworkUtils;

import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import retrofit2.HttpException;

public abstract class DefaultSubscriber<T> implements Observer<T> {

    @Override
    public void onError(Throwable e) {
        Throwable throwable = e;
        //获取最根源的异常
        while (throwable.getCause() != null) {
            e = throwable;
            throwable = throwable.getCause();
        }
        //无网络连接
        if (!NetworkUtils.isNetworkAvailable(AppContext.getContext())
                || e instanceof SocketTimeoutException) {
            onFailure(BaseURL.APP_EXCEPTION_HTTP_TIMEOUT, e.getMessage());
        } else if (e instanceof BusinessException) {
            //业务异常
            BusinessException exception = (BusinessException) e;
            onFailure(exception.getCode(), exception.getMessage());
        } else if (e instanceof HttpException) {
            //网络连接异常
            HttpException httpException = (HttpException) e;
            onFailure(String.valueOf(httpException.code()), httpException.getMessage());
        } else {
            //其他异常
            onFailure(BaseURL.APP_EXCEPTION_HTTP_OTHER, e.getMessage());
        }
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    public abstract void onSuccess(T t);

    public abstract void onFailure(String errorCode, String errorMessage);
}
