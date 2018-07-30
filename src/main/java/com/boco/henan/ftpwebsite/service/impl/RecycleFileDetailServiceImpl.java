package com.boco.henan.ftpwebsite.service.impl;

import com.boco.henan.ftpwebsite.dao.FileServerDetailMapper;
import com.boco.henan.ftpwebsite.dao.RecycleFileDetailMapper;
import com.boco.henan.ftpwebsite.entity.FileServerDetail;
import com.boco.henan.ftpwebsite.entity.Node;
import com.boco.henan.ftpwebsite.entity.RecycleFileDetail;
import com.boco.henan.ftpwebsite.service.RecycleFileDetailService;
import com.boco.henan.ftpwebsite.util.CUIDHexGenerator;
import com.boco.henan.ftpwebsite.util.FileUtil;
import com.boco.henan.ftpwebsite.util.RedisClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class RecycleFileDetailServiceImpl implements RecycleFileDetailService {

    private static final Logger LOGGER= LoggerFactory.getLogger(FileServerDetail.class);
    @Autowired
    private FileServerDetailMapper fileServerDetailMapper;
    @Autowired
    private RecycleFileDetailMapper recycleFileDetailMapper;
    @Value("${ftp.temp.dir}")
    private String deleteFtpFolder;
    @Autowired
    private RedisClient<Node> redisClient;

    @Override
    public void rebackReCycleFileDetail(String ids) throws Exception{
        String[] idsArr=ids.split(",");
        List<String> srcPathList=new ArrayList<>();
        List<String> destPathList=new ArrayList<>();
        for (int i=0;i< idsArr.length;i++){
            RecycleFileDetail recycleFileDetail=recycleFileDetailMapper.getById(idsArr[i]);
            FileServerDetail fileServerDetail=fileServerDetailMapper.getById(recycleFileDetail.getFileId());
            //fileServerDetailMapper.modifyByParentId(recycleFileDetail.getFileId());
            recycleFileDetailMapper.delete(idsArr[i]);

            String[] parentPath=recycleFileDetail.getRelativePath().split("\\\\");
            Node parentNode=redisClient.get("0");
            boolean isLost=false;
            //最后一个路径为自身
            for (int j=0;j<=parentPath.length-1;j++){
                List<FileServerDetail> destNodeTmpList=new ArrayList();
                if (!isLost) {
                    destNodeTmpList = fileServerDetailMapper.getByParentIdAndName(parentNode.getId(), parentPath[j]);
                }
                if (CollectionUtils.isEmpty(destNodeTmpList)){
                    isLost=true;
                    Node currentNode;
                    if (j!=parentPath.length-1) {
                        FileServerDetail fileServerDetail1Tmp = new FileServerDetail();
                        fileServerDetail1Tmp.setId(CUIDHexGenerator.getInstance().generate());
                        fileServerDetail1Tmp.setFileName(parentPath[j]);
                        fileServerDetail1Tmp.setCreateTime(new Timestamp(System.currentTimeMillis()));
                        fileServerDetail1Tmp.setParentId(parentNode.getId());
                        fileServerDetail1Tmp.setIsDelete("0");
                        fileServerDetail1Tmp.setIsFolder("1");
                        fileServerDetailMapper.save(fileServerDetail1Tmp);

                        currentNode = new Node(fileServerDetail1Tmp);
                    }else {
                        fileServerDetail.setModifyTime(new Timestamp(System.currentTimeMillis()));
                        fileServerDetail.setIsDelete("0");
                        fileServerDetail.setParentId(parentNode.getId());
                        fileServerDetailMapper.update(fileServerDetail);
                        fileServerDetailMapper.modifyByParentId(fileServerDetail.getId());

                        currentNode = new Node(fileServerDetail);
                    }
                    currentNode.setRelativePath(recycleFileDetail.getRelativePath());
                    currentNode.setNodePath(parentNode.getNodePath() + File.separator + currentNode.getName());
                    parentNode = currentNode;
                    redisClient.set(currentNode.getId(), currentNode);
                }else {
                    parentNode=redisClient.get(destNodeTmpList.get(0).getId());
                }

            }

            //修改当前结点id为新添加的节点id
            if (!isLost) {
                fileServerDetail.setId(recycleFileDetail.getFileId());
                fileServerDetailMapper.deleteById(recycleFileDetail.getFileId());
                changeChildrenParent(recycleFileDetail.getFileId(),parentNode);
            }
            srcPathList.add(deleteFtpFolder+File.separator+fileServerDetail.getFileName());
            destPathList.add(parentNode.getNodePath().replace(fileServerDetail.getFileName(),""));
            //FileUtil.moveFile(deleteFtpFolder+File.separator+fileServerDetail.getFileName(),parentNode.getNodePath().replace(fileServerDetail.getFileName(),""));
        }
        for (int j=0;j<srcPathList.size();j++){
            FileUtil.moveFile(srcPathList.get(j),destPathList.get(j));
        }
    }

    private void changeChildrenParent(String fileParentId,Node parentNode) {
        FileServerDetail fileServerDetailTmp=new FileServerDetail();
        fileServerDetailTmp.setParentId(fileParentId);
        List<FileServerDetail> fileServerDetailList=fileServerDetailMapper.getFileDetailList(fileServerDetailTmp);
        for (int i = 0; i <fileServerDetailList.size() ; i++) {
            FileServerDetail fileChildren=fileServerDetailList.get(i);
            List<FileServerDetail> tempList=fileServerDetailMapper.getByParentIdAndName(parentNode.getId(),fileChildren.getFileName());
            if (!CollectionUtils.isEmpty(tempList)){
                fileServerDetailMapper.deleteById(fileChildren.getId());
                parentNode=redisClient.get(tempList.get(0).getId());
                if ("1".equals(tempList.get(0).getIsFolder())){
                    changeChildrenParent(fileChildren.getId(),parentNode);
                }
            }else {
                fileChildren.setModifyTime(new Timestamp(System.currentTimeMillis()));
                fileChildren.setIsDelete("0");
                fileChildren.setParentId(parentNode.getId());
                fileServerDetailMapper.update(fileChildren);

                fileServerDetailMapper.modifyByParentId(fileChildren.getId());
            }
        }
    }


    @Override
    public void deleteReCycleFileDetail(String ids) throws Exception{
        for (String id:ids.split(",")){
            RecycleFileDetail recycleFileDetail=recycleFileDetailMapper.getById(id);
            fileServerDetailMapper.deleteByParentId(recycleFileDetail.getFileId());
            recycleFileDetailMapper.delete(id);
        }
        for (String id:ids.split(",")) {
            if (redisClient.containsKey(id)){
                Node currentNode=redisClient.get(id);
                FileUtil.delete(deleteFtpFolder+File.separator+currentNode.getName());
            }
        }
    }

    @Override
    public List<RecycleFileDetail> getList(RecycleFileDetail recycleFileDetail) {
        return recycleFileDetailMapper.getList(recycleFileDetail);
    }


    @Override
    public List<RecycleFileDetail> getDeleteList(int day) {
        return recycleFileDetailMapper.getDeleteList(day);
    }
}
