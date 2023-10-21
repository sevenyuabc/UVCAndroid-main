package com.cicada.kidscard.business.setting.domain;

/**
 * 区域信息
 * <p>
 * Create time: 2019-11-25 17:50
 *
 * @author liuyun.
 */
public class AreaInfo {

    /**
     * areaId : 区域id
     * areaName : 区域名称
     */

    private String id;
    private String areaName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
}
