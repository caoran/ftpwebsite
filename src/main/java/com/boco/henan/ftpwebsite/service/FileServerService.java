package com.boco.henan.ftpwebsite.service;

import com.boco.henan.ftpwebsite.entity.FileServerDetail;
import com.boco.henan.ftpwebsite.entity.ListResult;
import com.boco.henan.ftpwebsite.entity.Node;

import java.util.List;


public interface FileServerService {

    ListResult getFileDetailPage(FileServerDetail fileServerDetail);


    int save(FileServerDetail fileServerDetail);

    int delete(String id);


    int modify(FileServerDetail fileServerDetail);

    List<FileServerDetail> getList(FileServerDetail fileServerDetail);

    FileServerDetail getFileDetailById(String id);

    Node getFileTree(FileServerDetail fileServerDetail);
}
