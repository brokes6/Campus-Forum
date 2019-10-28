package com.example.bottomnavigationabar2;

import android.content.Intent;

import java.util.Date;

/**
 * 创建于2019/9/16 21:00🐎
 */
//1
public class Post {
    private Integer pid;
    private Integer puid;
    private String pcreateTime;
    //现在觉得不合适 后期修改 应该改为string
    private Integer ptag;
    private String content;
    private Integer plove;
    private Integer pviewNum;
    private String imgUrl;
    private String uimg;
    private String username;
    private boolean isShowAll = false;

    public boolean isShowAll() {
        return isShowAll;
    }

    public void setShowAll(boolean showAll) {
        isShowAll = showAll;
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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Integer getPuid() {
        return puid;
    }

    public void setPuid(Integer puid) {
        this.puid = puid;
    }

    public String getPcreateTime() {
        return pcreateTime;
    }

    public void setPcreateTime(String pcreateTime) {
        this.pcreateTime = pcreateTime;
    }

    public Integer getPtag() {
        return ptag;
    }

    public void setPtag(Integer ptag) {
        this.ptag = ptag;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getPlove() {
        return plove;
    }

    public void setPlove(Integer plove) {
        this.plove = plove;
    }

    public Integer getPviewNum() {
        return pviewNum;
    }

    public void setPviewNum(Integer pviewNum) {
        this.pviewNum = pviewNum;
    }
}