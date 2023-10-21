package com.cicada.kidscard.business.home.presenter;

import android.content.Context;

import com.cicada.kidscard.base.BasePresenter;
import com.cicada.kidscard.business.home.domain.BaseKidsCardInfoResponse;
import com.cicada.kidscard.business.home.model.ContactModel;
import com.cicada.kidscard.config.AppContext;
import com.cicada.kidscard.net.domain.Request;
import com.cicada.kidscard.net.retrofit.DefaultSubscriber;
import com.cicada.kidscard.net.retrofit.RetrofitUtils;
import com.cicada.kidscard.storage.db.DBKidsCardHelp;
import com.cicada.kidscard.storage.db.model.BaseKidsCardChildInfo;
import com.cicada.kidscard.storage.preferences.AppSharedPreferences;
import com.cicada.kidscard.utils.Preconditions;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ContactPresenter extends BasePresenter {
    private final Context mContext;
    private int pageIndex = 1;
    private final int pageSize = 500;

    private static ContactPresenter instance = null;
    private boolean querying = false;

    public static ContactPresenter getInstance() {
        if (instance == null) {
            synchronized (ContactPresenter.class) {
                instance = new ContactPresenter();
            }
        }
        return instance;
    }

    private ContactPresenter() {
        mContext = AppContext.getContext();
    }


    private void queryContactAllByPage() {
        pageIndex = 1;
        queryPageChildInfosBySchoolId();
    }

    /**
     * 分页拉取全量通讯录
     */
    private void queryPageChildInfosBySchoolId() {
        querying = true;
        String schoolId = AppSharedPreferences.getInstance().getKidsCardSchoolId();
        RetrofitUtils.createService(ContactModel.class)
                .queryPageChildInfosBySchoolId(new Request.Builder()
                        .withParam("schoolId", schoolId)
                        .withParam("pageIndex", pageIndex)
                        .withParam("pageSize", pageSize)
                        .build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscriber<BaseKidsCardInfoResponse>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        addDisposable(disposable);
                    }

                    @Override
                    public void onSuccess(BaseKidsCardInfoResponse result) {
                        querying = false;
                        pageIndex = result.getPage();
                        if (1 == pageIndex) {
                            DBKidsCardHelp.getInstance(mContext).clearKidsCardChildInfo();
                        }
                        List<BaseKidsCardChildInfo> mList = result.getRows();
                        if (Preconditions.isNotEmpty(mList)) {
                            DBKidsCardHelp.getInstance(mContext).saveKidsCardChildInfo(mList);
                        }

                        if (result.getPage() < result.getTotal()) {
                            pageIndex++;
                            queryPageChildInfosBySchoolId();
                        }
                    }

                    @Override
                    public void onFailure(String errorCode, String errorMessage) {
                        querying = false;
                    }
                });
    }

    /**
     * 同步通讯录
     * 本地没有通讯录:全量拉取一次
     * 本地有通讯录：增量
     */
    public void queryContactAdd() {
        if (querying) {
            return;
        }
        StringBuffer keys = new StringBuffer();
        List<BaseKidsCardChildInfo> childsinfo = DBKidsCardHelp.getInstance(mContext).findKidsCardChildInfo();
        if (Preconditions.isNotEmpty(childsinfo)) {
            for (int i = 0; i < childsinfo.size(); i++) {
                if (Preconditions.isNotEmpty(childsinfo.get(i).getMd5Key())) {
                    if (Preconditions.isNotEmpty(keys.toString())) {
                        keys.append(",");
                    }
                    keys.append(childsinfo.get(i).getMd5Key());
                }
            }
            queryInfosBySchoolIdAndMd5Key(keys.toString());
        } else {
            //本地没有通讯录:全量拉取一次
            queryContactAllByPage();
        }
    }

    /**
     * 增量拉取通讯录
     */
    private void queryInfosBySchoolIdAndMd5Key(String md5keys) {
        querying = true;
        String schoolId = AppSharedPreferences.getInstance().getKidsCardSchoolId();
        RetrofitUtils.createService(ContactModel.class)
                .queryInfosBySchoolIdAndMd5Key(new Request.Builder()
                        .withParam("schoolId", schoolId)
                        .withParam("md5Keys", md5keys)
                        .build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscriber<List<BaseKidsCardChildInfo>>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        addDisposable(disposable);
                    }

                    @Override
                    public void onSuccess(List<BaseKidsCardChildInfo> result) {
                        querying = false;
                        if (Preconditions.isNotEmpty(result)) {
                            List<BaseKidsCardChildInfo> updateList = new ArrayList<>();
                            List<String> deleteMd5keysList = new ArrayList<>();
                            for (BaseKidsCardChildInfo childInfo : result) {
                                if (1 == childInfo.getRecordStatus()) {//新增或者更新的数据
                                    updateList.add(childInfo);
                                } else if (Preconditions.isNotEmpty(childInfo.getMd5Key())) {//删除的数据
                                    deleteMd5keysList.add(childInfo.getMd5Key());
                                }
                            }
                            DBKidsCardHelp.getInstance(mContext).deleteKidsCardChildInfos(deleteMd5keysList);
                            DBKidsCardHelp.getInstance(mContext).deleteKidsCardChildInfoMd5IsNull();
                            DBKidsCardHelp.getInstance(mContext).saveKidsCardChildInfo(updateList);
                        }
                    }

                    @Override
                    public void onFailure(String errorCode, String errorMessage) {
                        querying = false;
                    }
                });
    }
}
