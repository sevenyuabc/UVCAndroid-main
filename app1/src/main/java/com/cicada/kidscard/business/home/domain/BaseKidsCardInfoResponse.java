package com.cicada.kidscard.business.home.domain;


import com.cicada.kidscard.storage.db.model.BaseKidsCardChildInfo;

import java.util.List;

/**
 * 分页查询通讯录返回model
 * <p>
 * Create time: 2021/5/14 11:54
 *
 * @author liuyun.
 */
public class BaseKidsCardInfoResponse {
    /**
     * conditions : {"status":{"data":1,"field":"status","op":"="},"groupOp":"and","schoolId":{"data":3101050054,"field":"schoolId","op":"="}}
     * page : 1
     * pagesize : 5
     * records : 35
     * rows : [{"md5Key":"037990153b26e76e061aa48e62285654","isTeacherCard":1,"teacherInfo":{"userId":117401,"userIcon":"http://static.imzhiliao.com/1495679448614vimKo5QCGQ.jpg","userName":"春花"},"recordStatus":1,"cardNumber":"0111616970"},{"childInfo":{"address":"","censusRegister":"","childClassId":28124,"childClassIds":"28124","childClassName":"(小)·杜月班","childIcon":"http://cicadafile.qiniudn.com/144099335234302UaFqKmZf.jpg","childId":116785,"childName":"候思钰","childSchoolId":3101050054,"childSchoolName":"蚂蚁国度","createDate":1491363217954,"gradeId":0,"isVipChild":0,"parentInfos":[{"childId":116785,"childName":"候思钰","classId":0,"createDate":1474442196653,"customerType":0,"extendsField":0,"isBindCard":0,"isHeader":0,"isOldUser":0,"phoneNum":"18500000698","relation":"妈妈","safeVipUser":false,"subjectId":0,"userIcon":"http://cicadafile.qiniudn.com/14409934560946PqYs3DU5X.jpg","userId":159200,"userName":"候思钰妈妈","userType":0}],"sequenceNumber":0,"sortNum":0},"md5Key":"76a445cffc56a12a0e24ebef93fb6808","isTeacherCard":0,"recordStatus":1,"cardNumber":"0106573514"},{"childInfo":{"address":"","censusRegister":"","childClassId":28125,"childClassIds":"28125","childClassName":"(小)·草芽班","childIcon":"http://cicadafile.qiniudn.com/1440924285913MKphFbgKYc.jpg","childId":116803,"childName":"相[=zhe2]玉润","childNamePinyin":"相[=zhe2]玉润","childSchoolId":3101050054,"childSchoolName":"蚂蚁国度","createDate":1496481448691,"gradeId":0,"isVipChild":0,"parentInfos":[{"childId":116803,"childName":"相玉润","classId":0,"createDate":1474442212100,"customerType":0,"extendsField":0,"isBindCard":0,"isHeader":0,"isOldUser":0,"phoneNum":"18500004778","relation":"妈妈","safeVipUser":false,"subjectId":0,"userIcon":"http://cicadafile.qiniudn.com/1440999605894unXyubb6ew.jpg","userId":159211,"userName":"郭玉润","userType":0}],"sequenceNumber":0,"sortNum":0},"md5Key":"42e73a42ebc4d4c19ada73667d7198ee","isTeacherCard":0,"recordStatus":1,"cardNumber":"0102344394"},{"childInfo":{"address":"","censusRegister":"","childClassId":28126,"childClassIds":"28126","childClassName":"(托)·青草班","childIcon":"http://cicadafile.qiniudn.com/1440900715957yugR0KfzFO.jpg","childId":116799,"childName":"陆秀","childNamePinyin":"","childSchoolId":3101050054,"childSchoolName":"蚂蚁国度","createDate":1496217604384,"gradeId":0,"isVipChild":1,"parentInfos":[{"childId":116799,"childName":"陆秀","classId":0,"createDate":1497950736686,"customerType":0,"extendsField":0,"isBindCard":0,"isHeader":0,"isOldUser":0,"phoneNum":"18500001242","relation":"奶奶","safeVipUser":false,"subjectId":0,"userIcon":"http://static.imzhiliao.com/Fk8RaHng-8KRD4uD3bB7inFGzVO8","userId":159203,"userName":"PLJZ0243","userType":0},{"childId":116799,"childName":"陆秀","classId":0,"createDate":1474442208296,"customerType":0,"extendsField":0,"isBindCard":0,"isHeader":0,"isOldUser":0,"phoneNum":"18500003370","relation":"妈妈","safeVipUser":false,"subjectId":0,"userIcon":"http://static.imzhiliao.com/FhfP-H5zM9r1N_lLcwRFkglVuPdw","userId":159221,"userName":"陆伶","userType":0},{"childId":116799,"childName":"陆秀","classId":0,"createDate":1497950245172,"customerType":0,"extendsField":0,"isBindCard":0,"isHeader":0,"isOldUser":0,"phoneNum":"18500000293","relation":"爷爷","safeVipUser":false,"subjectId":0,"userId":267509,"userName":"肩上蝶","userType":0}],"sequenceNumber":0,"sortNum":0},"md5Key":"418a723c55ccda3f240d90159f474ee9","isTeacherCard":0,"recordStatus":1,"cardNumber":"0369262928"},{"childInfo":{"address":"","censusRegister":"","childClassId":28125,"childClassIds":"28125","childClassName":"(小)·草芽班","childIcon":"http://cicadafile.qiniudn.com/14416126245516ubxlYOUKr.jpg","childId":116804,"childName":"李毅","childNamePinyin":"李毅","childSchoolId":3101050054,"childSchoolName":"蚂蚁国度","createDate":1501058812363,"gradeId":0,"isVipChild":0,"parentInfos":[{"childId":116804,"childName":"李毅","classId":0,"createDate":1474442212486,"customerType":0,"extendsField":0,"isBindCard":0,"isHeader":0,"isOldUser":0,"phoneNum":"18500005194","relation":"爸爸","safeVipUser":false,"subjectId":0,"userIcon":"http://cicadafile.qiniudn.com/1441619179172VGEwq9IgJ1.jpg","userId":159000,"userName":"海生","userType":0}],"sequenceNumber":0,"sortNum":0},"md5Key":"971208bcec6470657f77a69e96c0e1bc","isTeacherCard":0,"recordStatus":1,"cardNumber":"0184465879"}]
     * total : 7
     */

    private int page;
    private int pagesize;
    private int records;
    private int total;
    private List<BaseKidsCardChildInfo> rows;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public int getRecords() {
        return records;
    }

    public void setRecords(int records) {
        this.records = records;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<BaseKidsCardChildInfo> getRows() {
        return rows;
    }

    public void setRows(List<BaseKidsCardChildInfo> rows) {
        this.rows = rows;
    }
}
