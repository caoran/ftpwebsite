package com.boco.henan.ftpwebsite.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.boco.henan.ftpwebsite.entity.FileServerDetail;
import com.boco.henan.ftpwebsite.entity.ListResult;
import com.boco.henan.ftpwebsite.entity.Node;
import com.boco.henan.ftpwebsite.entity.RestResponse;
import com.boco.henan.ftpwebsite.exception.GeneralException;
import com.boco.henan.ftpwebsite.exception.PermissionExcpetion;
import com.boco.henan.ftpwebsite.exception.ValidationException;
import com.boco.henan.ftpwebsite.service.FileServerService;
import com.boco.henan.ftpwebsite.util.CUIDHexGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;

@Controller
public class FileServerDetailController {

    private static  final Logger logger= LoggerFactory.getLogger(MainController.class);


    @Autowired
    private FileServerService fileServerService;

    @RequestMapping(value = "/data/list",method = RequestMethod.GET)
    @ResponseBody
    public ListResult getFileDetailPage(@RequestParam String timeSearchType,FileServerDetail fileServerDetail){
        ListResult listResult=new ListResult();
        try {
            if (timeSearchType.equals("2")){
                fileServerDetail.setModifyStartTime(fileServerDetail.getIssueStartTime());
                fileServerDetail.setModifyEndTime(fileServerDetail.getIssueEndTime());
                fileServerDetail.setIssueStartTime(null);
                fileServerDetail.setIssueEndTime(null);
            }
            logger.info("获取分页查询结果，{}",fileServerDetail);
            listResult = fileServerService.getFileDetailPage(fileServerDetail);
        } catch (Exception e) {
            logger.error("获取{}所有page信息出错:" ,fileServerDetail,e);
        }
        logger.info("list查询返回结果为：{}", JSONArray.toJSONString(listResult));
        return listResult;
    }

    @RequestMapping(value = "/data/tree",method = RequestMethod.GET)
    public @ResponseBody Node  getFileTree(FileServerDetail fileServerDetail){
        Node node=fileServerService.getFileTree(fileServerDetail);
        logger.debug("获取tree数据为：{}", JSON.toJSONString(node));
        return node;
    }

    @RequestMapping(value = "/data/tree",method = RequestMethod.POST)
    @ResponseBody
    public RestResponse addFolder(@Valid FileServerDetail fileServerDetail, BindingResult bindingResult) throws Exception{
        logger.info("新建文件夹，{}",fileServerDetail);
        RestResponse<FileServerDetail> retRes=new RestResponse<>();
        try {
            if (bindingResult.hasErrors()){
                logger.info(JSON.toJSONString(bindingResult.getGlobalErrors()));
                retRes.setMessage(bindingResult.getFieldError().getDefaultMessage());
                retRes.setSuccess(false);
                 return retRes;
                //throw new PermissionExcpetion("AUTH02");
            }
            fileServerDetail.setId(CUIDHexGenerator.getInstance().generate());
            fileServerDetail.setCreateTime(new Timestamp(System.currentTimeMillis()));
            fileServerDetail.setIsFolder("1");
            fileServerDetail.setIsDelete("0");
            fileServerService.saveFolder(fileServerDetail);

            logger.info("获取文件保存路径为：{}",fileServerDetail.getFilePath());
            retRes.setSuccess(true);
            retRes.setMessage("新建文件夹成功");
            retRes.setData(fileServerDetail);
        } catch (GeneralException e){
            retRes.setSuccess(false);
            retRes.setMessage(e.getMessage());
            logger.error("",e);
        }catch (Exception e) {
            logger.error("新建文件夹出错:{}" ,e);
            retRes.setSuccess(false);
            retRes.setMessage("新建文件夹失败");
        }
        return retRes;
    }

    @RequestMapping(value = "/data/list",method = RequestMethod.POST)
    @ResponseBody
    public RestResponse save(@Valid FileServerDetail fileServerDetail, BindingResult bindingResult) throws Exception{
        logger.info("新增上传明细信息，{}",fileServerDetail);
        RestResponse<FileServerDetail> retRes=new RestResponse<>();
        try {
            if (bindingResult.hasErrors()){
                logger.info(JSON.toJSONString(bindingResult.getGlobalErrors()));
                retRes.setMessage(bindingResult.getFieldError().getDefaultMessage());
                retRes.setSuccess(false);
                 return retRes;
                //throw new PermissionExcpetion("AUTH02");
            }
            fileServerDetail.setId(CUIDHexGenerator.getInstance().generate());
            fileServerDetail.setCreateTime(new Timestamp(System.currentTimeMillis()));
            fileServerDetail.setIsFolder("0");
            fileServerDetail.setIsDelete("0");
            fileServerService.save(fileServerDetail);

            retRes.setSuccess(true);
            retRes.setMessage("新增上传明细信息成功");
            retRes.setData(fileServerDetail);
        }catch (GeneralException e){
            retRes.setSuccess(false);
            retRes.setMessage(e.getMessage());
            logger.error("",e);
        } catch (Exception e) {
            logger.error("新增上传明细信息出错:{}" ,e);
            retRes.setSuccess(false);
            retRes.setMessage("新增上传明细信息失败");
        }
        return retRes;
    }

    @RequestMapping(value = "/data/tree",method = RequestMethod.PUT)
    public @ResponseBody RestResponse<String> modify(@Valid FileServerDetail fileServerDetail, BindingResult bindingResult){
        logger.info("修改文件属性：{}",fileServerDetail);
        RestResponse<String> retRes=new RestResponse<>();
        try {
            if (bindingResult.hasErrors()){
                logger.info(JSON.toJSONString(bindingResult.getGlobalErrors()));
                retRes.setMessage(bindingResult.getFieldError().getDefaultMessage());
                retRes.setSuccess(false);
                return retRes;
                //throw new PermissionExcpetion("AUTH02");
            }
            fileServerDetail.setModifyTime(new Timestamp(System.currentTimeMillis()));
            fileServerService.modify(fileServerDetail);

            retRes.setSuccess(true);
            retRes.setMessage("修改文件属性成功");
        }catch (GeneralException e){
            retRes.setSuccess(false);
            retRes.setMessage(e.getMessage());
            logger.error("",e);
        }catch (Exception e){
            retRes.setSuccess(false);
            retRes.setMessage("修改文件属性失败");
            logger.error("",e);
        }
        return retRes;
    }


    @RequestMapping(value = "/data/{parentId}/list",method = RequestMethod.POST)
    public @ResponseBody RestResponse<String> modifyParent(@PathVariable String parentId, FileServerDetail fileServerDetail){
        logger.info("移动文件,parentId:{},fileServerDetail：{}",parentId,fileServerDetail);
        RestResponse<String> retRes=new RestResponse<>();
        try {
            fileServerDetail.setModifyTime(new Timestamp(System.currentTimeMillis()));
            fileServerDetail.setParentId(parentId);
            fileServerService.modifyParent(fileServerDetail);
            retRes.setSuccess(true);
            retRes.setMessage("移动文件成功");
        }catch (GeneralException e){
            retRes.setSuccess(false);
            retRes.setMessage(e.getMessage());
            logger.error("",e);
        }
        catch (Exception e){
            retRes.setSuccess(false);
            retRes.setMessage("移动文件失败");
            logger.error("",e);
        }
        return retRes;
    }


    @RequestMapping(value = "/data/tree",method = RequestMethod.DELETE)
    public @ResponseBody RestResponse<String> delete(@RequestParam String id){
        logger.info("删除节点，id为：{}",id);
        RestResponse<String> retRes=new RestResponse<>();
        try {
            FileServerDetail fileServerDetail=new FileServerDetail();
            fileServerDetail.setId(id);
            //删除状态
            fileServerDetail.setModifyTime(new Timestamp(System.currentTimeMillis()));
            fileServerDetail.setIsDelete("1");
            fileServerService.delete(id);
            retRes.setSuccess(Boolean.TRUE);
            retRes.setMessage("删除成功");
        }catch (Exception e){
            logger.error("",e);
            retRes.setSuccess(Boolean.FALSE);
            retRes.setMessage("删除失败");
        }
        return retRes;
    }

    @ResponseBody
    public void test() throws PermissionExcpetion{
        throw new PermissionExcpetion("AUTH02");
    }
}
