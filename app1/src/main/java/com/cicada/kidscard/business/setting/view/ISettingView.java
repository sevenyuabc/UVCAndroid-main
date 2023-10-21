package com.cicada.kidscard.business.setting.view;

import com.cicada.kidscard.base.IBaseView;
import com.cicada.kidscard.business.setting.domain.SchoolInfo;

/**
 * @ClassName: ISettingView
 * @Description: TODO
 * @Author: liuyun
 * @CreateDate: 2021/9/14 14:29
 * @UpdateUser: liuyun
 * @UpdateDate: 2021/9/14 14:29
 */
public interface ISettingView extends IBaseView {
    void findSchoolByIdResult(SchoolInfo result);

    void bindSchoolSuccess();
}
