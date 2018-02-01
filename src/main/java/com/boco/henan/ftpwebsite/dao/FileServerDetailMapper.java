package com.boco.henan.ftpwebsite.dao;

import com.boco.henan.ftpwebsite.entity.FileServerDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface FileServerDetailMapper {

    int deleteById(String id);

    int save(FileServerDetail record);

    FileServerDetail getById(String id);

    int update(FileServerDetail record);

    List<FileServerDetail> getFileDetailList(FileServerDetail fileServerDetail);

    List<FileServerDetail> getFileTree(FileServerDetail fileServerDetail);
}