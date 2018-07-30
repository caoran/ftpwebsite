package com.boco.henan.ftpwebsite.dao;

import com.boco.henan.ftpwebsite.entity.RecycleFileDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RecycleFileDetailMapper {

    int delete(String id);

    int save(RecycleFileDetail record);

    RecycleFileDetail getById(String id);

    int update(RecycleFileDetail record);

    List<RecycleFileDetail> getList(RecycleFileDetail recycleFileDetail);

    List<RecycleFileDetail> getDeleteList(@Param("day") int day);
}