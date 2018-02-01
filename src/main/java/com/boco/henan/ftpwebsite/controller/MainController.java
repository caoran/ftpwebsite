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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.sql.Timestamp;


@Controller
public class MainController {

    private static  final Logger logger= LoggerFactory.getLogger(MainController.class);


    @Autowired
    private FileServerService fileServerService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${ftp.temp.dir}")
    private String deleteFtpFolder;

    @RequestMapping(value = "/",method = RequestMethod.GET)
    public String index(){
        return "index";
    }


    @RequestMapping(value = "/data/list",method = RequestMethod.GET)
    @ResponseBody
    public ListResult getFileDetailPage(FileServerDetail fileServerDetail){
        ListResult listResult=new ListResult();
        logger.info("获取分页查询结果，{}",fileServerDetail);
        try {
            listResult = fileServerService.getFileDetailPage(fileServerDetail);
        } catch (Exception e) {
            logger.info("获取{}所有page信息出错:" ,fileServerDetail);
        }
        logger.info("list查询返回结果为：{}",JSONArray.toJSONString(listResult));
        return listResult;
    }

    @RequestMapping(value = "/data/list/recycle",method = RequestMethod.GET)
    @ResponseBody
    public ListResult getDeleteFileDetailPage(FileServerDetail fileServerDetail){
        ListResult listResult=new ListResult();
        logger.info("获取删除分页查询结果，{}",fileServerDetail);
        try {
            listResult = fileServerService.getFileDetailPage(fileServerDetail);
        } catch (Exception e) {
            logger.info("获取{}所有page信息出错:" ,fileServerDetail);
        }
        logger.info("list查询返回结果为：{}",JSONArray.toJSONString(listResult));
        return listResult;
    }

    @RequestMapping(value = "/data/{id}/detail",method = RequestMethod.GET)
    public String getFileDetailPage(@PathVariable String id, ModelMap modelMap){
        logger.info("获取文件详细信息，id为：{}",id);
        modelMap.put("data",fileServerService.getFileDetailById(id));
        return "/folder/detailform";
    }


    @RequestMapping(value = "/data/list/folder",method = RequestMethod.GET)
    public String getFileFolderForm(ModelMap modelMap){
        logger.info("获取文件移动页面，跳转/folder/moveform");
        return "/folder/moveform";
    }

    @RequestMapping(value = "/data/recycle",method = RequestMethod.GET)
    public String getRecycleFileList(ModelMap modelMap){
        logger.info("获取文件移动页面，跳转/folder/moveform");
        return "/folder/recycleList";
    }


    @RequestMapping(value = "/data/tree",method = RequestMethod.GET)
    public @ResponseBody Node  getFileTree(FileServerDetail fileServerDetail){
        Node node=fileServerService.getFileTree(fileServerDetail);
        logger.info("获取tree数据为：{}", JSON.toJSONString(node));
        return node;
    }

    @RequestMapping(value = "/data/tree",method = RequestMethod.POST)
    @ResponseBody
    public FileServerDetail addFolder(FileServerDetail fileServerDetail){
        logger.info("新建文件夹，{}",fileServerDetail);
        try {
            fileServerDetail.setId(CUIDHexGenerator.getInstance().generate());
            fileServerDetail.setCreateTime(new Timestamp(System.currentTimeMillis()));
            fileServerDetail.setIsFolder(Short.parseShort("1"));
            fileServerDetail.setIsDelete(Short.parseShort("0"));
            fileServerService.save(fileServerDetail);
            FileUtil.createNewFile(fileServerDetail.getFilePath(),false);
            logger.info("获取文件保存路径为：{}",fileServerDetail.getFilePath());
        } catch (Exception e) {
            logger.error("新建文件夹出错:{}" ,e);
        }
        return fileServerDetail;
    }

    @RequestMapping(value = "/data/tree",method = RequestMethod.PUT)
    public @ResponseBody RestResponse<String> modify(FileServerDetail fileServerDetail){
        logger.info("修改文件属性：{}",fileServerDetail);
        RestResponse<String> retRes=new RestResponse<>();
        try {
            fileServerDetail.setModifyTime(new Timestamp(System.currentTimeMillis()));
            fileServerService.modify(fileServerDetail);
            if (!fileServerDetail.getFileName().equals(fileServerDetail.getOldFileName())) {
                FileUtil.changeFileName(fileServerDetail.getFileName(), fileServerDetail.getFilePath());
            }
            retRes.setSuccess(true);
            retRes.setMessage("修改文件属性成功");
        }catch (Exception e){
            retRes.setSuccess(false);
            retRes.setMessage("修改文件属性失败");
            logger.error("",e);
        }
        return retRes;
    }


    @RequestMapping(value = "/data/{parentId}/list",method = RequestMethod.POST)
    public @ResponseBody RestResponse<String> modifyDetail(@PathVariable String parentId, FileServerDetail fileServerDetail){
        logger.info("移动文件,parentId:{},fileServerDetail：{}",parentId,fileServerDetail);
        RestResponse<String> retRes=new RestResponse<>();
        try {
            fileServerDetail.setModifyTime(new Timestamp(System.currentTimeMillis()));
            fileServerDetail.setParentId(parentId);
            fileServerService.modify(fileServerDetail);

            String[] ids=fileServerDetail.getId().split(",");
            for (String nodeId:ids) {
                ValueOperations<String, Node> operations=redisTemplate.opsForValue();
                if (redisTemplate.hasKey(nodeId)&&redisTemplate.hasKey(parentId)) {
                    Node node=operations.get(nodeId);
                    Node parentNode=operations.get(nodeId);
                    FileUtil.moveFile(node.getNodePath(),parentNode.getNodePath());
                }
            }

            retRes.setSuccess(true);
            retRes.setMessage("移动文件成功");
        }catch (Exception e){
            retRes.setSuccess(false);
            retRes.setMessage("移动文件失败");
            logger.error("",e);
        }
        return retRes;
    }


    @RequestMapping(value = "/data/tree/{id}",method = RequestMethod.DELETE)
    public @ResponseBody RestResponse<String> delete(@PathVariable String id){
        logger.info("删除节点，id为：{}",id);
        RestResponse<String> retRes=new RestResponse<>();
        try {
            FileServerDetail fileServerDetail=new FileServerDetail();
            fileServerDetail.setId(id);
            //删除状态
            fileServerDetail.setModifyTime(new Timestamp(System.currentTimeMillis()));
            fileServerDetail.setIsDelete(Short.valueOf("1"));
            fileServerService.modify(fileServerDetail);
            retRes.setSuccess(Boolean.FALSE);
            retRes.setMessage("删除成功");

            String[] ids=fileServerDetail.getId().split(",");
            for (String nodeId:ids) {
                ValueOperations<String, Node> operations=redisTemplate.opsForValue();
                if (redisTemplate.hasKey(nodeId)) {
                    Node node=operations.get(nodeId);
                    FileUtil.moveFile(node.getNodePath(),deleteFtpFolder+ File.separator+node.getName());
                }
            }
        }catch (Exception e){
            logger.error("",e);
            retRes.setSuccess(Boolean.FALSE);
            retRes.setMessage("删除失败");
        }
        return retRes;
    }
}
