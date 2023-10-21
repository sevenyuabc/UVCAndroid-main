package com.cicada.kidscard.view.banner;

/**
 * TODO
 * <p>
 * Create time: 2021/5/12 10:18
 *
 * @author liuyun.
 */
public class BannerInfo {

    private int resId;
    private String machinePics;

    public BannerInfo() {
    }
    public BannerInfo(String machinePics) {
        this.machinePics = machinePics;
    }

    public BannerInfo(int resId) {
        this.resId = resId;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }


    public String getMachinePics() {
        return machinePics;
    }

    public void setMachinePics(String machinePics) {
        this.machinePics = machinePics;
    }
}
