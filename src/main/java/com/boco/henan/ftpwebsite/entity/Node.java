package com.boco.henan.ftpwebsite.entity;

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

    public Node(){}

    public Node(String id, String name,String parentId,Boolean isParent, List<Node> children,String nodePath) {
        this.name = name;
        this.id = id;
        this.children=children;
        this.parentId=parentId;
        this.isParent=isParent;
        this.nodePath=nodePath;
    }

    public Node(String id, String name,String parentId,Boolean isParent,String nodePath) {
        this(id,name,parentId,isParent,new ArrayList<Node>(),nodePath);
    }
    public Node(String id, String name,String parentId,String nodePath){
        this(id,name,parentId,Boolean.FALSE,nodePath);
    }

    public Node(String id, String name,Boolean isParent,String nodePath){
        this(id,name,"",isParent,nodePath);
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

    @Override
    public String toString() {
        return "Node{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", isParent='" + isParent + '\'' +
                ", parentId='" + parentId + '\'' +
                ", children=" + children +
                '}';
    }
}
