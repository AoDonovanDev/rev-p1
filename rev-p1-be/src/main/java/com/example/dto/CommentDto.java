package com.example.dto;

public class CommentDto {
    Integer commentId;
    Integer cmPostId;
    Integer commentedBy;
    String commentText;
    
    public CommentDto(Integer commentId, Integer cmPostId, Integer commentedBy, String commentText) {
        this.commentId = commentId;
        this.cmPostId = cmPostId;
        this.commentedBy = commentedBy;
        this.commentText = commentText;
    }
    public Integer getCommentId() {
        return commentId;
    }
    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }
    public Integer getCmPostId() {
        return cmPostId;
    }
    public void setCmPostId(Integer cmPostId) {
        this.cmPostId = cmPostId;
    }
    public Integer getCommentedBy() {
        return commentedBy;
    }
    public void setCommentedBy(Integer commentedBy) {
        this.commentedBy = commentedBy;
    }
    public String getCommentText() {
        return commentText;
    }
    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

}
