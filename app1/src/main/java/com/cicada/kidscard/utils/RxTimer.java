package com.cicada.kidscard.utils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RxTimer {


    private RxTimer() {

    }

    private static class Holder {
        private static final RxTimer instance = new RxTimer();

    }

    public static RxTimer getInstance() {
        return Holder.instance;
    }

    private Disposable disposable;

    /**
     * delay时间后执行特定任务
     */
    public void excuteTask(long delay, final DoAction doAction) {
        Observable.timer(delay, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(@NonNull Long aLong) {
                        if (doAction != null) {
                            doAction.action(aLong);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                    }
                });


    }
    /**
     * delay时间后执行特定任务
     */
    public void excuteSecondTask(long delay, final DoAction doAction) {
        Observable.timer(delay, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(@NonNull Long aLong) {
                        if (doAction != null) {
                            doAction.action(aLong);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                    }
                });


    }

    /*****每,隔多少时间去执行任务*****/

    public void interVal(long delay, final DoAction doAction) {
        Observable.interval(delay, TimeUnit.MINUTES)
                //指定观察者在Android主线程执行
                .observeOn(AndroidSchedulers.mainThread())
                //指定被观察者在子线程执行
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(Long aLong) {
                        if (doAction != null) {
                            doAction.action(aLong);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    /*****每,隔多少时间去执行任务*****/

    public void interValSeconds(long delay, final DoAction doAction) {
        Observable.interval(delay, TimeUnit.SECONDS)
                //指定观察者在Android主线程执行
                .observeOn(AndroidSchedulers.mainThread())
                //指定被观察者在子线程执行
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(Long aLong) {
                        if (doAction != null) {
                            doAction.action(aLong);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }



    /***在Activity销毁时一定要调用cancelTimer否则会造成内存泄露**/
    public void cancelTimer() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }


    public interface DoAction {
        void action(long count);
    }

}
