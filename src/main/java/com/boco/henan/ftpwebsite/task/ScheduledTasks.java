package com.boco.henan.ftpwebsite.task;

import com.alibaba.fastjson.JSON;
import com.boco.henan.ftpwebsite.entity.RecycleFileDetail;
import com.boco.henan.ftpwebsite.service.RecycleFileDetailService;
import com.boco.henan.ftpwebsite.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class ScheduledTasks {

    private static final Logger logger= LoggerFactory.getLogger(ScheduledTasks.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Value("${ftp.temp.dir}")
    private String deleteFtpFolder;

    @Autowired
    private RecycleFileDetailService recycleFileDetailService;

    /**
     * 在上面的入门例子中，使用了@Scheduled(fixedRate = 5000) 注解来定义每过5秒执行的任务，对于@Scheduled的使用可以总结如下几种方式：

     @Scheduled(fixedRate = 5000) ：上一次开始执行时间点之后5秒再执行
     @Scheduled(fixedDelay = 5000) ：上一次执行完毕时间点之后5秒再执行
     @Scheduled(initialDelay=1000, fixedRate=5000) ：第一次延迟1秒后执行，之后按fixedRate的规则每5秒执行一次 **/
     //@Scheduled(cron="*/5 * * * * *") ：通过cron表达式定义规则


    @Scheduled(cron = "* 0/20 0 * * *")  //每天0点-1点之间运行，并每隔10分钟运行一次
    public void reportCurrentTime() {
        System.out.println("当前时间：" + dateFormat.format(new Date()));
        try {
            List<String> srcPathList=new ArrayList<>();
            List<RecycleFileDetail> recycleFileList = recycleFileDetailService.getDeleteList(7);
            logger.info("查询七天前删除的文件和文件夹：list", JSON.toJSONString(recycleFileList));
            String ids = "";
            for (RecycleFileDetail recycleFileDetail : recycleFileList) {
                ids += "," + recycleFileDetail.getId();
                String relativePath=recycleFileDetail.getRelativePath();
                String fileName=relativePath.substring(relativePath.lastIndexOf("\\")+1);
                srcPathList.add(fileName);
            }
            recycleFileDetailService.deleteReCycleFileDetail(ids);
            for (int j=0;j<srcPathList.size();j++){
                FileUtil.delete(deleteFtpFolder+ File.separator+srcPathList.get(j));
            }
            logger.info("删除七天前的文件成功！");
        }catch (Exception e){
            e.printStackTrace();
            logger.error("除七天前的文件失败",e);
        }

    }



}
