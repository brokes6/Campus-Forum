package com.example.bottomnavigationabar2.bean;

import com.example.bottomnavigationabar2.Post;

import java.util.List;

/**
 * 创建在 2019/11/27 14:39
 */
public class SearchDto {
    private List<Organization> organizationList;
    private List<Post> postList;

    public List<Organization> getOrganizationList() {
        return organizationList;
    }

    public void setOrganizationList(List<Organization> organizationList) {
        this.organizationList = organizationList;
    }

    public List<Post> getPostList() {
        return postList;
    }

    public void setPostList(List<Post> postList) {
        this.postList = postList;
    }
}
