package com.boco.henan.ftpwebsite.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.Random;

public class FileUtil {
    private static final Logger LOGGER= LoggerFactory.getLogger(FileUtil.class);

    private static final String DELETE_TMP_BACK="../../temp/ftp/back";

    /**
     * 创建文件
     * @param file_path
     */
    public static void createNewFile(String file_path,boolean is_file) throws java.io.IOException{
        LOGGER.info("创建文件夹：{}",file_path);
        File myFilePath = new File(file_path);
        try {
            if (!myFilePath.exists()) {
                if (!is_file){
                    myFilePath.mkdirs();
                }else {
                    if (!myFilePath.getParentFile().exists()){
                        myFilePath.getParentFile().mkdirs();
                    }
                    myFilePath.createNewFile();
                }
            }
        }
        catch (Exception e) {
            LOGGER.error("新建文件操作出错");
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * move file
     *
     * @param srcFilePath
     * @param destFilePath
     */
    public static void moveFile(String srcFilePath, String destFilePath) throws java.io.IOException{
        try {
            File srcFile=new File(srcFilePath);
            File destFile=new File(destFilePath);
            if (srcFile.exists()) {
                LOGGER.info("移动文件，源文件为：{}，目标文件为：{}", srcFilePath, destFilePath);
                if (srcFile.isDirectory()) {
                    FileUtils.copyDirectoryToDirectory(srcFile, destFile);
                } else {
                    FileUtils.copyFileToDirectory(srcFile, destFile, true);
                }
                delete(srcFilePath);
            }
        }
        catch (IOException e) {
            LOGGER.error("移动文件操作出错");
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * copy file
     *
     * @param srcFile
     * @param destFile
     */
    public static void copy(File srcFile, File destFile) throws java.io.IOException{
        try {
            LOGGER.info("复制文件，源路径为：{}，目标路径为：{}",srcFile.getPath(),destFile.getPath());
            if (srcFile.isDirectory()) {
                FileUtils.copyDirectoryToDirectory(srcFile, destFile);
            }else {
                FileUtils.copyFileToDirectory(srcFile, destFile, true);
            }
        }
        catch (IOException e) {
            LOGGER.error("复制文件操作出错");
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 删除 file
     *
     * @param destFilePath
     */
    public static void delete(String destFilePath) throws java.io.IOException{
        try {
            LOGGER.info("删除文件，路径为：{}", destFilePath);
            File file = new File(destFilePath);
            if (file.exists()) {
                int randomNum = (int) (1 + Math.random() * (10 - 1 + 1));
                Path path = Paths.get(DELETE_TMP_BACK, String.valueOf(System.currentTimeMillis()) + "_" + randomNum);
                if (Files.notExists(path)) {
                    Files.createDirectories(path);
                    LOGGER.info("备份文件，源地址：{}，目的地址：{}", destFilePath, path.toAbsolutePath().toString());
                    copy(new File(destFilePath), path.toFile());
                    FileUtils.forceDelete(new File(destFilePath));
                }
            }else {
                LOGGER.info("删除文件，路径为：【{}】不存在，删除成功", destFilePath);
            }
        }
        catch (IOException e) {
            LOGGER.error("删除文件操作出错");
            e.printStackTrace();
            throw e;
        }
    }


    /**
     * get file size
     * <ul>
     * <li>if path is null or empty, return -1</li>
     * <li>if path exist and it is a file, return file size, else return -1</li>
     * <ul>
     *
     * @param path
     * @return returns the length of this file in bytes. returns -1 if the file does not exist.
     */
    public static String getFileSize(String path) throws java.io.IOException{
        try {
            LOGGER.info("获取文件大小，路径为：{}",path);
            if (StringUtils.isBlank(path)) {
                return "0KB";
            }

            File file = new File(path);
            //long size= (file.exists() && file.isFile() ? file.length() : -1);
            long size=FileUtils.sizeOf(file);
            System.out.printf("size:"+size);
            return getSize(size,0);
        }
        catch (Exception e) {
            LOGGER.error("删除文件操作出错");
            e.printStackTrace();
            throw e;
        }
    }

    private static String getSize(float size,int count) {
        DecimalFormat fnum = new  DecimalFormat("##0.00");
        if(count==0){
            size=size/1024;
        }
        if (size>1024&&count<3){
            size=size/1024;
            return getSize(size,++count);
        }
        String result=fnum.format(size)+" ";
        if (count==0) result+="KB";
        if (count==1) result+="MB";
        if (count==2) result+="GB";

        return result;
    }


    public static void changeFileName(String newFileName,String filePath) throws Exception {
        LOGGER.info("修改文件名称，文件名称{}，文件路径为：{}", newFileName, filePath);
        try {
            File oldFile = new File(filePath);
            File newFile = new File(oldFile.getParentFile().getPath() + File.separator + newFileName);
            if (oldFile.exists() && !newFile.exists() && oldFile.renameTo(newFile)) {
                LOGGER.info("文件名称修改成功");
            }
        }
        catch (Exception e) {
            LOGGER.error("删除文件操作出错");
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 获取文件后缀
     * @param fileName
     * @return
     * @throws Exception
     */
    public static String getFileSuffix(String fileName){
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        return suffix;
    }
}
