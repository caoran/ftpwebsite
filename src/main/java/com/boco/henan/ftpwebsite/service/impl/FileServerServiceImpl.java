package com.boco.henan.ftpwebsite.service.impl;

import com.boco.henan.ftpwebsite.dao.FileServerDetailMapper;
import com.boco.henan.ftpwebsite.entity.FileServerDetail;
import com.boco.henan.ftpwebsite.entity.ListResult;
import com.boco.henan.ftpwebsite.entity.Node;
import com.boco.henan.ftpwebsite.service.FileServerService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class FileServerServiceImpl implements FileServerService {
    private static final Logger LOGGER= LoggerFactory.getLogger(FileServerDetail.class);
    @Autowired
    private FileServerDetailMapper fileServerDetailMapper;
    @Value("${ftp.upload.dir}")
    private String  ftpFolder;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public ListResult getFileDetailPage(FileServerDetail fileServerDetail) {
        Map<String,Object> resultMap=new HashMap<>();
        Page<FileServerDetail> page=PageHelper.startPage(fileServerDetail.getPageNumber(),fileServerDetail.getPageSize());
        List<FileServerDetail> list=fileServerDetailMapper.getFileDetailList(fileServerDetail);
        ListResult result = new ListResult(page.getTotal(), page.getResult());
        return result;
    }

    @Override
    public Node getFileTree(FileServerDetail fileServerDetail) {
        List<FileServerDetail> list=fileServerDetailMapper.getFileTree(fileServerDetail);
        Node root=new Node("0","根节点",Boolean.TRUE,ftpFolder);
        Node current=root;
        Map<String,Node> nodeMap=new HashMap<>();
        for (FileServerDetail detail:list){
            nodeMap.put(current.getId(),current);
            redisTemplate.opsForValue().set(current.getId(),current);
            while (!detail.getParentId().equals(current.getId())){
                if (StringUtils.isEmpty(current.getParentId())) break;
                current=nodeMap.get(detail.getParentId());
            }
            Node nodeTmp=new Node(detail.getId(),detail.getFileName(),current.getId(),detail.getIsFolder()==1,current.getNodePath()+ File.separator+detail.getFileName());
            current.getChildren().add(nodeTmp);
            //current.setIsParent(Boolean.TRUE);
            current=nodeTmp;
        }
        return root;
    }

    @Override
    public int save(FileServerDetail fileServerDetail) {
        return fileServerDetailMapper.save(fileServerDetail);
    }

    @Override
    public int delete(String id) {
        return fileServerDetailMapper.deleteById(id);
    }

    @Override
    public int modify(FileServerDetail fileServerDetail) {
        String[] ids=fileServerDetail.getId().split(",");
        for (String id:ids) {
            FileServerDetail fileServerDetailTmp = new FileServerDetail();
            BeanUtils.copyProperties(fileServerDetail, fileServerDetailTmp);
            fileServerDetailTmp.setId(id);
            fileServerDetailMapper.update(fileServerDetail);
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
