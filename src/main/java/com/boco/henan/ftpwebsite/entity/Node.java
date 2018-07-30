package com.boco.henan.ftpwebsite.entity;

import com.boco.henan.ftpwebsite.util.FileUtil;
import com.boco.henan.ftpwebsite.util.RedisClient;
import com.boco.henan.ftpwebsite.util.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.thymeleaf.util.ArrayUtils;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Node implements Serializable{
    private static final long serialVersionUID = 3714827586565908167L;

    private String name;
    private String id;
    private String parentId;
    private Boolean isParent;
    private List<Node> children;
    private String nodePath;
    private String relativePath;

    private String icon;

    //private RedisClient<Node> redisClient= (RedisClient<Node>)SpringUtil.getBean("redisClient");

    public Node(){}


    public Node(FileServerDetail fileServerDetail){
        this(fileServerDetail.getId(),fileServerDetail.getFileName(),fileServerDetail.getParentId(),true);
        this.relativePath=fileServerDetail.getRelativePath();
    }

    public Node(String id, String name,String parentId,Boolean isParent, List<Node> children,String nodePath){
        this.name = name;
        this.id = id;
        this.children=children;
        this.parentId=parentId;
        this.isParent=isParent;
        this.nodePath=nodePath;

        initIcon();
    }

    public Node(String id, String name,String parentId,Boolean isParent, List<Node> children) {
       this(id,name,parentId,isParent,children,"");
        //this.nodePath=redisClient.get(parentId).nodePath+ File.separator+name;
    }

    public Node(String id, String name,String parentId,Boolean isParent) {
        this(id,name,parentId,isParent,new ArrayList<Node>());
    }
    public Node(String id, String name,String parentId){
        this(id,name,parentId,Boolean.FALSE);
    }

    public Node(String id, String name,Boolean isParent){
        this(id,name,"0",isParent);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    public Boolean getIsParent() {
        return isParent;
    }

    public void setIsParent(Boolean isParent) {
        this.isParent = isParent;
    }

    public String getNodePath() {
        return nodePath;
    }

    public void setNodePath(String nodePath) {
        this.nodePath = nodePath;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    private void initIcon(){
        if (isParent) return;
        String suffix= FileUtil.getFileSuffix(this.name);
        System.out.printf("suffix========================"+suffix);
        String[] rarArr={"rar","zip","tar","iso","7z"};
        if (ArrayUtils.contains(rarArr,suffix)){
            this.icon="/images/yasuo.png";
        }
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "Node{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", isParent='" + isParent + '\'' +
                ", parentId='" + parentId + '\'' +
                ", icon='" + icon + '\'' +
                ", children=" + children +
                '}';
    }
}
