package com.boco.henan.ftpwebsite.service;

import com.boco.henan.ftpwebsite.entity.FileServerDetail;
import com.boco.henan.ftpwebsite.entity.ListResult;
import com.boco.henan.ftpwebsite.entity.Node;

import java.io.IOException;
import java.util.List;


public interface FileServerService {

    ListResult getFileDetailPage(FileServerDetail fileServerDetail);


    int saveFolder(FileServerDetail fileServerDetail) throws Exception;

    int save(FileServerDetail fileServerDetail) throws Exception;

    int delete(String id) throws IOException;


    int modify(FileServerDetail fileServerDetail) throws Exception;

    int modifyParent(FileServerDetail fileServerDetail) throws Exception;

    List<FileServerDetail> getList(FileServerDetail fileServerDetail);

    ListResult getRecycleFileDetailPage(FileServerDetail fileServerDetail);

    FileServerDetail getFileDetailById(String id);

    Node getFileTree(FileServerDetail fileServerDetail);

}
