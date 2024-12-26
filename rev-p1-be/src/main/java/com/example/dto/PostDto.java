package com.example.dto;

public class PostDto {
    private Integer postId;
    private String username;
    private String postText;
    private Long timePostedEpoch;
    private Integer postedBy;
    
    public PostDto(){}

    public PostDto(Integer postId, Integer postedBy, String postText, Long timePostedEpoch ) {
        this.postId = postId;
        this.postText = postText;
        this.timePostedEpoch = timePostedEpoch;
        this.postedBy = postedBy;
    }
    public PostDto(Integer postId, String username, String postText, Long timePostedEpoch, Integer postedBy) {
        this.postId = postId;
        this.username = username;
        this.postText = postText;
        this.timePostedEpoch = timePostedEpoch;
        this.postedBy = postedBy;
    }
    public Integer getPostedBy() {
        return postedBy;
    }
    public void setPostedBy(Integer postedBy) {
        this.postedBy = postedBy;
    }
    public Integer getPostId() {
        return postId;
    }
    public void setPostId(Integer postId) {
        this.postId = postId;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPostText() {
        return postText;
    }
    public void setPostText(String postText) {
        this.postText = postText;
    }
    public Long getTimePostedEpoch() {
        return timePostedEpoch;
    }
    public void setTimePostedEpoch(Long timePostedEpoch) {
        this.timePostedEpoch = timePostedEpoch;
    }

    public String getAccount(){
        return username;
    }
}
