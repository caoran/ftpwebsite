package com.boco.henan.ftpwebsite.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.boco.henan.ftpwebsite.entity.FileServerDetail;
import com.boco.henan.ftpwebsite.entity.ListResult;
import com.boco.henan.ftpwebsite.entity.Node;
import com.boco.henan.ftpwebsite.entity.RestResponse;
import com.boco.henan.ftpwebsite.service.FileServerService;
import com.boco.henan.ftpwebsite.util.CUIDHexGenerator;
import com.boco.henan.ftpwebsite.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.sql.Timestamp;


@Controller
public class MainController {

    private static  final Logger logger= LoggerFactory.getLogger(MainController.class);

    @Autowired
    private FileServerService fileServerService;

    @RequestMapping(value = "/",method = RequestMethod.GET)
    public String index(){
        return "index";
    }


    @RequestMapping(value = "/data/list/folder",method = RequestMethod.GET)
    public String getFileFolderForm(ModelMap modelMap){
        logger.info("获取文件移动页面，跳转/folder/moveform");
        return "folder/moveform";
    }

    @RequestMapping(value = "/data/recycle",method = RequestMethod.GET)
    public String getRecycleFileList(ModelMap modelMap){
        logger.info("获取文件移动页面，跳转/folder/moveform");
        return "folder/recycleList";
    }


    @RequestMapping(value = "/data/{id}/detail",method = RequestMethod.GET)
    public String getFileDetailPage(@PathVariable String id, ModelMap modelMap){
        logger.info("获取文件详细信息，id为：{}",id);
        modelMap.put("data",fileServerService.getFileDetailById(id));
        return "folder/detailform";
    }


}
