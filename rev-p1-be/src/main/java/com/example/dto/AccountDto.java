package com.example.dto;
import com.example.entity.Post;
import java.util.List;


public class AccountDto {
    private Integer accountId;
    private String username;
    private List<Post> posts;
    
    public AccountDto(Integer accountId, String username, List<Post> posts) {
        this.accountId = accountId;
        this.username = username;
        this.posts = posts;
    }
    public Integer getAccountId() {
        return accountId;
    }
    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public List<Post> getPosts() {
        return posts;
    }
    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}
