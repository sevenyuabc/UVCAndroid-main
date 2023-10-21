package com.cicada.kidscard.base;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class BasePresenter implements BasePresenterImpl {

    private CompositeDisposable mCompositeDisposable;

    protected void addDisposable(Disposable disposable) {
        if (this.mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable() ;
        }
        this.mCompositeDisposable.add(disposable);
    }

    @Override
    public void undispose() {
        if (this.mCompositeDisposable != null) {
            this.mCompositeDisposable.dispose();
        }
    }
}
