package com.cicada.kidscard.net;

/**
 * Created by Target.Fan on 2021/3/5 5:35 PM
 */
public abstract class RequestResult<T> {

    public abstract void onSuccess(T result);

    public abstract void onFailure();

}
