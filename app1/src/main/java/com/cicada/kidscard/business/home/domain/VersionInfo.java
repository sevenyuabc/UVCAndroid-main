package com.cicada.kidscard.business.home.domain;

public class VersionInfo {
    /**
     * 升级状态： 0 表示已是最新 1 检测到新版本升级 2 检测到强制升级版本
     */
    private int updateType;

    /**
     * 升级版本
     */
    private String version;

    /**
     * 升级编号
     */
    private long versionCode;

    /**
     * 升级介绍
     */
    private String versionIntro;
    /**
     * 升级版本下载地址
     */
    private String downLoadUrl;
    /**
     * 升级包发布时间
     */
    private long createDate;
    /**
     * 升级包大小
     */
    private String versionSize;

    public int getUpdateType() {
        return updateType;
    }

    public void setUpdateType(int updateType) {
        this.updateType = updateType;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public long getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(long versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionIntro() {
        return versionIntro;
    }

    public void setVersionIntro(String versionIntro) {
        this.versionIntro = versionIntro;
    }

    public String getDownLoadUrl() {
        return downLoadUrl;
    }

    public void setDownLoadUrl(String downLoadUrl) {
        this.downLoadUrl = downLoadUrl;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public String getVersionSize() {
        return versionSize;
    }

    public void setVersionSize(String versionSize) {
        this.versionSize = versionSize;
    }
}
