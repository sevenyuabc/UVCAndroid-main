package com.cicada.kidscard.business.home.view;


import com.cicada.kidscard.business.home.domain.FileInfo;

import java.util.List;

/**
 * upload
 * <p>
 * Create time: 2021/5/13 10:42
 *
 * @author liuyun.
 */
public interface IUploadView {
    void  uploadSuccess(List<FileInfo> fileInfoList);
    void  uploadFailed();
}
