package com.example.bottomnavigationabar2.bean;


import java.util.Date;

/**
 * 创建在 2019/9/9 16:41
 */
public class Organization {
    private int oid;
    private String oname;
    private String account;
    private String password;
    private int onum;
    private int ceoId;
    private String createTime;
    private String ointroduce;
    private String oimg;
    private int loveNum;
    private int postNum;
    public int getPostNum() {
        return postNum;
    }

    public void setPostNum(int postNum) {
        this.postNum = postNum;
    }

    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    public String getOname() {
        return oname;
    }

    public void setOname(String oname) {
        this.oname = oname;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getOnum() {
        return onum;
    }

    public void setOnum(int onum) {
        this.onum = onum;
    }

    public int getCeoId() {
        return ceoId;
    }

    public void setCeoId(int ceoId) {
        this.ceoId = ceoId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getOintroduce() {
        return ointroduce;
    }

    public void setOintroduce(String ointroduce) {
        this.ointroduce = ointroduce;
    }

    public String getOimg() {
        return oimg;
    }

    public void setOimg(String oimg) {
        this.oimg = oimg;
    }

    public int getLoveNum() {
        return loveNum;
    }

    public void setLoveNum(int loveNum) {
        this.loveNum = loveNum;
    }
}
