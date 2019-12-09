package com.example.bottomnavigationabar2.bean;


import java.util.Date;

/**
 * 创建在 2019/11/6 17:17
 */
public class UserMessage {
    private int id;
    private int uid;
    private int tid;
    private String createTime;
    private int action;
    private String content;
    private String repliesContent;
    private int status;
    private String uimg;
    private String username;
    private String mapId;

    public String getMapId() {
        return mapId;
    }

    public void setMapId(String mapId) {
        this.mapId = mapId;
    }

    public String getUimg() {
        return uimg;
    }

    public void setUimg(String uimg) {
        this.uimg = uimg;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRepliesContent() {
        return repliesContent;
    }

    public void setRepliesContent(String repliesContent) {
        this.repliesContent = repliesContent;
    }
}
