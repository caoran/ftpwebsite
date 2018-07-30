package com.boco.henan.ftpwebsite.service;

import com.boco.henan.ftpwebsite.entity.RecycleFileDetail;

import java.util.List;

public interface RecycleFileDetailService {

    void  rebackReCycleFileDetail(String ids) throws Exception;

    void deleteReCycleFileDetail(String ids) throws Exception;

    List<RecycleFileDetail>  getList(RecycleFileDetail recycleFileDetail);

    List<RecycleFileDetail>  getDeleteList(int day);

}
