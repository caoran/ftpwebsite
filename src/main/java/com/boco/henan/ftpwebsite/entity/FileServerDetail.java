package com.boco.henan.ftpwebsite.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.util.Date;

public class FileServerDetail {
    private String id;

    @NotEmpty(message = "文件名称不能为空")
    //@Size(min = 1, max = 50, message = "文件名称长度必须大于 1 且小于 50 字")
    private String fileName;

    private String oldFileName;

    private String busiNo;

    private String fileNo;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date issueTime;

    private String fileSize;

    private String isFolder;

    private String parentId;

    private String isDelete;

    private String filePath;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    private String createUserId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date modifyTime;

    private String modifyUserId;
    //开始页
    private int pageNumber;
    //每页数据
    private int pageSize;
    //下发开始时间
    private String issueStartTime;
    //下发结束时间
    private String issueEndTime;
    //修改开始时间
    private String modifyStartTime;
    //修改结束时间
    private String modifyEndTime;

    private String fileId;

    private String relativePath;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = (fileName == null||fileName.isEmpty()) ? null : fileName.trim();
    }

    public String getBusiNo() {
        return busiNo;
    }

    public void setBusiNo(String busiNo) {
        this.busiNo = (busiNo == null||busiNo.isEmpty()) ? null : busiNo.trim();
    }

    public String getFileNo() {
        return fileNo;
    }

    public void setFileNo(String fileNo) {
        this.fileNo = (fileNo == null||fileNo.isEmpty()) ? null : fileNo.trim();
    }

    public Date getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(Date issueTime) {
        this.issueTime = issueTime;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize == null ? null : fileSize.trim();
    }

    public String getIsFolder() {
        return isFolder;
    }

    public void setIsFolder(String isFolder) {
        this.isFolder = isFolder;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId == null ? null : parentId.trim();
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath == null ? null : filePath.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId == null ? null : createUserId.trim();
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getModifyUserId() {
        return modifyUserId;
    }

    public void setModifyUserId(String modifyUserId) {
        this.modifyUserId = modifyUserId == null ? null : modifyUserId.trim();
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getIssueStartTime() {
        return issueStartTime;
    }

    public void setIssueStartTime(String issueStartTime) {
        this.issueStartTime = issueStartTime;
    }

    public String getIssueEndTime() {
        return issueEndTime;
    }

    public void setIssueEndTime(String issueEndTime) {
        this.issueEndTime = issueEndTime;
    }

    public String getModifyStartTime() {
        return modifyStartTime;
    }

    public void setModifyStartTime(String modifyStartTime) {
        this.modifyStartTime = modifyStartTime;
    }

    public String getModifyEndTime() {
        return modifyEndTime;
    }

    public void setModifyEndTime(String modifyEndTime) {
        this.modifyEndTime = modifyEndTime;
    }

    public String getOldFileName() {
        return oldFileName;
    }

    public void setOldFileName(String oldFileName) {
        this.oldFileName = oldFileName;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    @Override
    public String toString() {
        return "FileServerDetail{" +
                "id='" + id + '\'' +
                ", fileName='" + fileName + '\'' +
                ", oldFileName='" + oldFileName + '\'' +
                ", busiNo='" + busiNo + '\'' +
                ", fileNo='" + fileNo + '\'' +
                ", issueTime=" + issueTime +
                ", fileSize='" + fileSize + '\'' +
                ", isFolder=" + isFolder +
                ", parentId='" + parentId + '\'' +
                ", isDelete=" + isDelete +
                ", filePath='" + filePath + '\'' +
                ", createTime=" + createTime +
                ", createUserId='" + createUserId + '\'' +
                ", modifyTime=" + modifyTime +
                ", modifyUserId='" + modifyUserId + '\'' +
                ", pageSize=" + pageSize +
                ", pageNumber=" + pageNumber +
                ", issueStartTime='" + issueStartTime + '\'' +
                ", issueEndTime='" + issueEndTime + '\'' +
                ", modifyStartTime='" + modifyStartTime + '\'' +
                ", modifyEndTime='" + modifyEndTime + '\'' +
                ", fileId='" + fileId + '\'' +
                ", relativePath='" + relativePath + '\'' +
                '}';
    }
}