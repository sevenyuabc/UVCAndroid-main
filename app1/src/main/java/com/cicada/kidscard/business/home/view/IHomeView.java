package com.cicada.kidscard.business.home.view;


import com.cicada.kidscard.base.IBaseView;
import com.cicada.kidscard.storage.db.model.BaseKidsCardChildInfo;

public interface IHomeView extends IBaseView {

    void getCardInfoSuccess(BaseKidsCardChildInfo info);

    void showCardUserInfo(String userName, String className, String userIcon);
}
