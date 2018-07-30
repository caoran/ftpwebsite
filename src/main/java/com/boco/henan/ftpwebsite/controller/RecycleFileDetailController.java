package com.boco.henan.ftpwebsite.controller;

import com.alibaba.fastjson.JSONArray;
import com.boco.henan.ftpwebsite.entity.FileServerDetail;
import com.boco.henan.ftpwebsite.entity.ListResult;
import com.boco.henan.ftpwebsite.entity.Node;
import com.boco.henan.ftpwebsite.entity.RestResponse;
import com.boco.henan.ftpwebsite.service.FileServerService;
import com.boco.henan.ftpwebsite.service.RecycleFileDetailService;
import com.boco.henan.ftpwebsite.util.RedisClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RecycleFileDetailController {


    private static  final Logger logger= LoggerFactory.getLogger(RecycleFileDetailController.class);


    @Autowired
    private FileServerService fileServerService;

    @Autowired
    private RecycleFileDetailService recycleFileDetailService;

    @Value("${ftp.temp.dir}")
    private String deleteFtpFolder;

    @Autowired
    private RedisClient<Node> redisClient;


    @RequestMapping(value = "/data/list/recycle",method = RequestMethod.GET)
    public ListResult getDeleteFileDetailPage(FileServerDetail fileServerDetail){
        ListResult listResult=new ListResult();
        logger.info("获取删除分页查询结果，{}",fileServerDetail);
        try {
            listResult = fileServerService.getRecycleFileDetailPage(fileServerDetail);
        } catch (Exception e) {
            logger.info("获取{}所有page信息出错:" ,fileServerDetail);
        }
        logger.info("list查询返回结果为：{}",JSONArray.toJSONString(listResult));
        return listResult;
    }

    @RequestMapping(value = "/data/list/recycle",method = RequestMethod.PUT)
    public RestResponse rebackReCycleFileDetail(@RequestParam(value = "id") String ids){
        RestResponse<String> retRes=new RestResponse<>();
        logger.info("恢复选中文件id：{}",ids);
        try {
            recycleFileDetailService.rebackReCycleFileDetail(ids);
            retRes.setMessage("恢复成功");
            retRes.setSuccess(true);
        } catch (Exception e) {
            retRes.setMessage("恢复失败");
            retRes.setSuccess(false);
            logger.info("恢复选中文件信息出错:" ,e);
        }
        return retRes;
    }

    @RequestMapping(value = "/data/list/recycle",method = RequestMethod.DELETE)
    public RestResponse deleteReCycleFileDetail(@RequestParam(value = "id") String ids){
        RestResponse<String> retRes=new RestResponse<>();
        logger.info("删除选中文件id：{}",ids);
        try {
            recycleFileDetailService.deleteReCycleFileDetail(ids);
            retRes.setMessage("删除成功");
            retRes.setSuccess(true);
        } catch (Exception e) {
            retRes.setMessage("删除失败");
            retRes.setSuccess(false);
            logger.error("删除选中文件信息出错:" ,e);
        }
        return retRes;
    }
}
