package com.boco.henan.ftpwebsite.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class FileUtil {
    private static final Logger LOGGER= LoggerFactory.getLogger(FileUtil.class);

    /**
     * 创建文件
     * @param file_path
     */
    public static void createNewFile(String file_path,boolean is_file) {
        LOGGER.info("创建文件夹：{}",file_path);
        File myFilePath = new File(file_path);
        try {
            if (!myFilePath.exists()) {
                if (!is_file){
                    myFilePath.mkdirs();
                }else {
                    myFilePath.createNewFile();
                }
            }
        }
        catch (Exception e) {
            LOGGER.error("新建文件操作出错");
            e.printStackTrace();
        }
    }

    /**
     * move file
     *
     * @param srcFile
     * @param destFile
     */
    public static void moveFile(String srcFile, String destFile) {
        try {
        FileUtils.moveFile(new File(srcFile),new File(destFile));
        }
        catch (IOException e) {
            LOGGER.error("移动文件操作出错");
            e.printStackTrace();
        }
    }

    /**
     * copy file
     *
     * @param srcFile
     * @param destFile
     */
    public static void copy(File srcFile, File destFile) {
        try {
            FileUtils.copyDirectory(srcFile,destFile);
        }
        catch (IOException e) {
            LOGGER.error("复制文件操作出错");
            e.printStackTrace();
        }
    }

    /**
     * 删除 file
     *
     * @param destFile
     */
    public static void delete(File destFile) {
        try {
            FileUtils.deleteDirectory(destFile);
        }
        catch (IOException e) {
            LOGGER.error("删除文件操作出错");
            e.printStackTrace();
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
    public static long getFileSize(String path) {
        if (StringUtils.isBlank(path)) {
            return -1;
        }

        File file = new File(path);
        return (file.exists() && file.isFile() ? file.length() : -1);
    }


    public static void changeFileName(String newFileName,String filePath){
        LOGGER.info("修改文件名称，文件名称{}，文件路径为：{}",newFileName,filePath);
        File oldFile=new File(filePath);
        File newFile=new File(oldFile.getParentFile().getPath()+File.separator+newFileName);
        if (oldFile.exists()&&!newFile.exists()&&oldFile.renameTo(newFile)){
            LOGGER.info("文件名称修改成功");
        }
    }
}
