package com.boco.henan.ftpwebsite.service.impl;

import com.boco.henan.ftpwebsite.dao.FileServerDetailMapper;
import com.boco.henan.ftpwebsite.dao.RecycleFileDetailMapper;
import com.boco.henan.ftpwebsite.entity.FileServerDetail;
import com.boco.henan.ftpwebsite.entity.ListResult;
import com.boco.henan.ftpwebsite.entity.Node;
import com.boco.henan.ftpwebsite.entity.RecycleFileDetail;
import com.boco.henan.ftpwebsite.exception.ValidationException;
import com.boco.henan.ftpwebsite.service.FileServerService;
import com.boco.henan.ftpwebsite.util.CUIDHexGenerator;
import com.boco.henan.ftpwebsite.util.FileUtil;
import com.boco.henan.ftpwebsite.util.RedisClient;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)
public class FileServerServiceImpl implements FileServerService {
    private static final Logger LOGGER= LoggerFactory.getLogger(FileServerDetail.class);
    @Autowired
    private FileServerDetailMapper fileServerDetailMapper;
    @Autowired
    private RecycleFileDetailMapper recycleFileDetailMapper;
    @Autowired
    private RedisClient<Node> redisClient;
    @Value("${ftp.upload.dir}")
    private String  ftpFolder;
    @Value("${ftp.temp.dir}")
    private String deleteFtpFolder;

    @Override
    public ListResult getFileDetailPage(FileServerDetail fileServerDetail) {
        Page<FileServerDetail> page=PageHelper.startPage(fileServerDetail.getPageNumber(),fileServerDetail.getPageSize());
        List<FileServerDetail> list=fileServerDetailMapper.getFileDetailList(fileServerDetail);
        ListResult result = new ListResult(page.getTotal(), page.getResult());
        return result;
    }

    private String setCurrentNodePath(Node node){
        String filePath="";
        if (redisClient.containsKey(node.getParentId())){
            if(!node.getId().equals("0")){
                filePath=redisClient.get(node.getParentId()).getNodePath() + File.separator + node.getName();
            }else {
                filePath=ftpFolder;
            }
            node.setNodePath(filePath);
        }
        redisClient.set(node.getId(),node);
        return filePath;
    }

    @Override
    public Node getFileTree(FileServerDetail fileServerDetail) {
        List<FileServerDetail> list=fileServerDetailMapper.getFileTree(fileServerDetail);
        Node root=new Node("0","根目录","",true,new ArrayList<>(),ftpFolder);
        Node current=root;
        redisClient.set(current.getId(),current);
        Map<String,Node> nodeMap=new HashMap<>();
        for (FileServerDetail detail:list){
            nodeMap.put(current.getId(),current);
            while (!detail.getParentId().equals(current.getId())){
                if (StringUtils.isEmpty(current.getParentId())) break;
                current=nodeMap.get(detail.getParentId());
            }
            Node nodeTmp=new Node(detail.getId(),detail.getFileName(),current.getId(),detail.getIsFolder().equals("1"));
            current.getChildren().add(nodeTmp);
            nodeTmp.setNodePath(current.getNodePath()+File.separator+nodeTmp.getName());
            //current.setIsParent(Boolean.TRUE);
            //setCurrentNodePath(nodeTmp,current.getNodePath());
            current=nodeTmp;
            redisClient.set(current.getId(),current);
        }
        return root;
    }

    @Override
    public ListResult getRecycleFileDetailPage(FileServerDetail fileServerDetail) {
        Page<FileServerDetail> page=PageHelper.startPage(fileServerDetail.getPageNumber(),fileServerDetail.getPageSize());
        List<FileServerDetail> list=fileServerDetailMapper.getRecycleFileDetailList(fileServerDetail);
        for (FileServerDetail temp:list){
            redisClient.set(temp.getId(),new Node(temp));
        }
        ListResult result = new ListResult(page.getTotal(), page.getResult());
        return result;
    }

    @Override
    public  int saveFolder(FileServerDetail fileServerDetail) throws Exception{
        int result= this.save(fileServerDetail);
        Node currentNode=new Node(fileServerDetail);
        redisClient.set(currentNode.getId(),currentNode);
        setCurrentNodePath(currentNode);
        FileUtil.createNewFile(currentNode.getNodePath(),false);
        return result;
    }

    @Override
    public  int save(FileServerDetail fileServerDetail) throws Exception{
        //判断同一父目录下是否存在同名文件夹
        List<FileServerDetail> sameNameFileList=fileServerDetailMapper.getByParentIdAndName(fileServerDetail.getParentId(),fileServerDetail.getFileName());
        if (!CollectionUtils.isEmpty(sameNameFileList)) {
            throw new ValidationException("目录下包含同名文件"+fileServerDetail.getFileName()+",新建文件失败");
        }
        int result= fileServerDetailMapper.save(fileServerDetail);
        return result;
    }

    @Override
    public int delete(String ids) throws IOException{
        String[] idsArr=ids.split(",");
        FileServerDetail fileServerDetail;
        List<String> srcPathList=new ArrayList<>();
        String destPath = deleteFtpFolder + File.separator;
        List<String> deletePathList=new ArrayList<>();
        for (String id:idsArr) {
            fileServerDetail = fileServerDetailMapper.getById(id);
            fileServerDetail.setIsDelete("1");
            fileServerDetail.setModifyTime(new Timestamp(System.currentTimeMillis()));
            if (fileServerDetail.getIsFolder().equals("1")) {
                fileServerDetailMapper.modifyDeleteByParentId(id);
            }
            fileServerDetail.setParentId("");
            fileServerDetailMapper.update(fileServerDetail);
            //判断删除表中是否包含相同的文件名称
            FileServerDetail fileServerDetailTmp=new FileServerDetail();
            fileServerDetailTmp.setFileName(fileServerDetail.getFileName());
            List<FileServerDetail> fileServerDetailList=fileServerDetailMapper.getRecycleFileDetailList(fileServerDetailTmp);
            //删除表中已经包含同名称的文件，先删除该文件
            if (!CollectionUtils.isEmpty(fileServerDetailList)){
                for (FileServerDetail temp:fileServerDetailList){
                    //从删除表中删除
                    recycleFileDetailMapper.delete(temp.getId());
                    fileServerDetailMapper.deleteByParentId(temp.getFileId());
                    fileServerDetailMapper.deleteById(temp.getFileId());
                    deletePathList.add(deleteFtpFolder+File.separator+fileServerDetail.getFileName());
                }
            }


            //文件为删除状态，加入删除信息表
            RecycleFileDetail recycleFileDetail = new RecycleFileDetail();
            recycleFileDetail.setId(CUIDHexGenerator.getInstance().generate());
            recycleFileDetail.setFileId(id);
            recycleFileDetail.setDeletedate(new Timestamp(System.currentTimeMillis()));
            if (redisClient.containsKey(id)) {
                recycleFileDetail.setRelativePath(redisClient.get(id).getNodePath().replace(ftpFolder+File.separator, ""));
            }

            recycleFileDetailMapper.save(recycleFileDetail);
            Node currentNode=redisClient.get(id);
           /* for (int j=0;j<deletePathList.size();j++){
                FileUtil.delete(deletePathList.get(j));
            }*/
            srcPathList.add(currentNode.getNodePath());
            //FileUtil.moveFile(currentNode.getNodePath(), destPath);
            //return fileServerDetailMapper.deleteById(id);
           /* currentNode.setNodePath(destPath);
            redisClient.set(currentNode.getId(),currentNode);*/
        }

        for (int j=0;j<deletePathList.size();j++){
            FileUtil.delete(deletePathList.get(j));
        }
        for (int j=0;j<srcPathList.size();j++){
            FileUtil.moveFile(srcPathList.get(j), destPath);
        }
        return idsArr.length;
    }

    @Override
    public int modify(FileServerDetail fileServerDetail) throws Exception{
        //当文件夹名称发生变化时，修改文件名称
        if (fileServerDetail.getFileName()!=null&&!fileServerDetail.getFileName().equals(fileServerDetail.getOldFileName())) {
            //判断同一父目录下是否存在同名文件夹
            List<FileServerDetail> sameNameFileList=fileServerDetailMapper.getByParentIdAndName(fileServerDetail.getId(),fileServerDetail.getFileName());
            if (!CollectionUtils.isEmpty(sameNameFileList)) {
                throw new ValidationException("目录下包含同名文件"+fileServerDetail.getFileName()+",修改文件失败");
            }
            Node currentNode = redisClient.get(fileServerDetail.getId());
            String originalFilePath = currentNode.getNodePath();
            currentNode.setName(fileServerDetail.getFileName());
            currentNode.setNodePath(redisClient.get(currentNode.getParentId()).getNodePath() + File.separator + currentNode.getName());
            redisClient.set(currentNode.getId(), currentNode);
            FileUtil.changeFileName(fileServerDetail.getFileName(), originalFilePath);
        }
        int size= fileServerDetailMapper.update(fileServerDetail);
        return size;
    }


    @Override
    public int modifyParent(FileServerDetail fileServerDetail) throws Exception{
        String[] ids=fileServerDetail.getId().split(",");
        for (String id:ids) {
            FileServerDetail fileServerDetailTmp = fileServerDetailMapper.getById(id);
            List<FileServerDetail> list=fileServerDetailMapper.getByParentIdAndName(fileServerDetail.getParentId(),fileServerDetailTmp.getFileName());
            if (!CollectionUtils.isEmpty(list)){
                throw new ValidationException("目标目录下包含同名文件"+fileServerDetailTmp.getFileName()+",移动文件失败");
            }
            FileServerDetail temp=new FileServerDetail();
            BeanUtils.copyProperties(fileServerDetail, temp);
            temp.setId(id);
            fileServerDetailMapper.update(temp);
        }

        //移动文件夹
        for (String nodeId : ids) {
            Node currentNode = redisClient.get(nodeId);
            String destPath = redisClient.get(fileServerDetail.getParentId()).getNodePath();
            FileUtil.moveFile(currentNode.getNodePath(), destPath);
        }
        return ids.length;
    }


    @Override
    public List<FileServerDetail> getList(FileServerDetail fileServerDetail) {
        return fileServerDetailMapper.getFileDetailList(fileServerDetail);
    }

    @Override
    public FileServerDetail getFileDetailById(String id) {
        FileServerDetail fileServerDetail=new FileServerDetail();
        fileServerDetail.setId(id);
        List<FileServerDetail> list=fileServerDetailMapper.getFileDetailList(fileServerDetail);
        return list.size()>0?list.get(0):null;
    }

}
