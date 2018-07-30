package com.boco.henan.ftpwebsite.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class FileUtilTest {
    
    @Test
    public void createNewFile() throws Exception {
        String filePath="D:\\test\\root\\1\\2\\3\\4\\7.txt";
        FileUtil.createNewFile(filePath,true);

        String folderPath="D:\\test\\root\\folder\\2\\3\\4\\5";
        FileUtil.createNewFile(folderPath,false);
    }

    @Test
    public void moveFile() throws Exception {
        String srcFile="D:\\tmp\\delete\\新建 Microsoft Excel 工作表.xlsx";
        String destPath="D:\\ftp\\1";
        FileUtil.moveFile(srcFile,destPath);
    }

    @Test
    public void copy() throws Exception {
    }

    @Test
    public void delete() throws Exception {
        String destFilePath="D:\\test\\root\\folder\\2\\3\\4\\5\\新建 Microsoft Excel 工作表.xlsx";
        FileUtil.delete(destFilePath);
    }

    @Test
    public void getFileSize() throws Exception {
        String destFilePath="D:\\soap_check_all-3.3.1.jar";
        String size=FileUtil.getFileSize(destFilePath);
        System.out.printf("文件大小为："+size);

    }

    @Test
    public void changeFileName() throws Exception {
    }

    @Test
    public  void testSubFileName(){
        String relativePath="2\\3";
        String fileName=relativePath.substring(relativePath.lastIndexOf("\\")+1);
        System.out.printf(fileName);
    }
}