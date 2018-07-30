package com.boco.henan.ftpwebsite.dao;

import com.boco.henan.ftpwebsite.entity.FileServerDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface FileServerDetailMapper {

    int deleteById(String id);

    int save(FileServerDetail record);

    FileServerDetail getById(String id);

    int update(FileServerDetail record);

    List<FileServerDetail> getFileDetailList(FileServerDetail fileServerDetail);

    List<FileServerDetail> getRecycleFileDetailList(FileServerDetail fileServerDetail);

    List<FileServerDetail> getFileTree(FileServerDetail fileServerDetail);

    void deleteByParentId(String id);

    void modifyByParentId(String id);

    void modifyDeleteByParentId(String id);

    List getByParentIdAndName(@Param("id") String id,@Param("fileName") String fileName);
}