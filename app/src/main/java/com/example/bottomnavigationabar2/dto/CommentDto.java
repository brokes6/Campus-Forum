package com.example.bottomnavigationabar2.dto;

import java.util.Date;

public class CommentDto {
    private int cid;
    private int puid;
    private int cuid;
    private int cpid;
    private Date ccreateTime;
    private String content;
    private int love_count;
    private String postContent;
    private String username;
    private String userImg;

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }
    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPuid() {
        return puid;
    }

    public void setPuid(int puid) {
        this.puid = puid;
    }

    public int getCuid() {
        return cuid;
    }

    public void setCuid(int cuid) {
        this.cuid = cuid;
    }

    public int getCpid() {
        return cpid;
    }

    public void setCpid(int cpid) {
        this.cpid = cpid;
    }

    public Date getCcreateTime() {
        return ccreateTime;
    }

    public void setCcreateTime(Date ccreateTime) {
        this.ccreateTime = ccreateTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLove_count() {
        return love_count;
    }

    public void setLove_count(int love_count) {
        this.love_count = love_count;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }
}
