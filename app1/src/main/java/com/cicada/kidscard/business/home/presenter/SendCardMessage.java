package com.cicada.kidscard.business.home.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.cicada.kidscard.app.MyApplication;
import com.cicada.kidscard.business.home.domain.CardRecordIdInfo;
import com.cicada.kidscard.business.home.domain.FileInfo;
import com.cicada.kidscard.business.home.domain.event.EmsHasCardCount;
import com.cicada.kidscard.business.home.model.MainModel;
import com.cicada.kidscard.business.home.view.IUploadView;
import com.cicada.kidscard.net.domain.Request;
import com.cicada.kidscard.net.retrofit.DefaultSubscriber;
import com.cicada.kidscard.net.retrofit.RetrofitUtils;
import com.cicada.kidscard.storage.db.DBKidsCardHelp;
import com.cicada.kidscard.storage.db.model.BaseKidsCardRecord;
import com.cicada.kidscard.storage.db.model.BaseKidsCardTakePhoto;
import com.cicada.kidscard.utils.LogUtils;
import com.cicada.kidscard.utils.Preconditions;
import com.tamsiree.rxtool.RxFileTool;
import com.tamsiree.rxtool.RxTool;
import com.tamsiree.rxtool.interfaces.OnSimpleListener;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Target.Fan on 2021/1/27 1:55 PM
 */
public class SendCardMessage {

    private static SendCardMessage instance = null;
    private Context mContext;
    private boolean sending = false;

    public static SendCardMessage getInstance() {
        if (instance == null) {
            synchronized (SendCardMessage.class) {
                instance = new SendCardMessage();
            }
        }
        return instance;
    }

    private SendCardMessage() {
        LogUtils.e("=========", "SendCardMessage");
        mContext = MyApplication.getInstance().getApplicationContext();
    }


    /**
     * 上传刷卡记录
     */
    public void send() {
        LogUtils.d("=========", "SendCardMessage:" + sending);
        if (sending) {
            return;
        }
        BaseKidsCardRecord baseKidsCardRecord = DBKidsCardHelp.getInstance(mContext).findFirstCardRecord();
        if (Preconditions.isNotEmpty(baseKidsCardRecord)) {
            sending = true;
            compressedUploadFile(baseKidsCardRecord);
        }
    }


    /**
     * 文件上传&上传刷卡记录
     *
     * @param baseKidsCardRecord
     */
    public void compressedUploadFile(BaseKidsCardRecord baseKidsCardRecord) {
        if (Preconditions.isNotEmpty(baseKidsCardRecord.getUserIcon()) && RxFileTool.fileExists(baseKidsCardRecord.getUserIcon())) {
            new UploadPresenter(new IUploadView() {
                @Override
                public void uploadSuccess(List<FileInfo> fileInfoList) {
                    baseKidsCardRecord.setUserIcon(fileInfoList.get(0).getUrl());
                    sendCardRecordMessage(baseKidsCardRecord);
                }

                @Override
                public void uploadFailed() {
                    sendCardRecordMessage(baseKidsCardRecord);
                }
            }).uploadPostFile(baseKidsCardRecord.getUserIcon());
        } else {
            sendCardRecordMessage(baseKidsCardRecord);
        }

    }


    /**
     * 发送刷卡记录
     *
     * @param baseKidsCardRecord
     */
    private void sendCardRecordMessage(final BaseKidsCardRecord baseKidsCardRecord) {
        RetrofitUtils.createService(MainModel.class)
                .uploadCardRecord(new Request.Builder()
                        .withParam("schoolId", baseKidsCardRecord.getSchoolId())
                        .withParam("record", JSON.toJSONString(baseKidsCardRecord))
                        .build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscriber<CardRecordIdInfo>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                    }

                    @Override
                    public void onSuccess(CardRecordIdInfo result) {
                        if (Preconditions.isNotEmpty(result)) {
                            saveUploadFailPhoto(result, baseKidsCardRecord);
                        }
                        if (Preconditions.isNotEmpty(baseKidsCardRecord)) {
                            DBKidsCardHelp.getInstance(mContext).deleteKidsCardRecord(baseKidsCardRecord.getLocalId());
                        }
                        sendNext();
                    }

                    @Override
                    public void onFailure(String errorCode, String errorMessage) {
                        sendNext();
                    }
                });
    }

    private void sendNext() {
        sending = false;
        RxTool.delayToDo(1000, new OnSimpleListener() {
            @Override
            public void doSomething() {
                send();
            }
        });
        RxTool.delayToDo(3000, new OnSimpleListener() {
            @Override
            public void doSomething() {
                EventBus.getDefault().post(new EmsHasCardCount());
            }
        });
    }


    /**
     * 保存上传失败图片
     *
     * @param result
     * @param baseKidsCardRecord
     */
    private void saveUploadFailPhoto(CardRecordIdInfo result, BaseKidsCardRecord baseKidsCardRecord) {
        if (Preconditions.isNotEmpty(result.getCardRecordId())) {
            BaseKidsCardTakePhoto baseKidscardPhoto = new BaseKidsCardTakePhoto();
            baseKidscardPhoto.setRecordId(result.getCardRecordId());
            if (!TextUtils.isEmpty(baseKidsCardRecord.getUserIcon())) {
                baseKidscardPhoto.setPhotoPath(baseKidsCardRecord.getUserIcon());
                DBKidsCardHelp.getInstance(mContext).addSendPhotoErrorRecord(baseKidscardPhoto);
            }
        }
    }
}
