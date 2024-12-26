package com.example.entity;
import javax.persistence.*;

@Entity
@Table(name="comment")
public class Comment {
    
    @Column (name="commentId")
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer commentId;

    @Column (name="cmPostId")
    private Integer cmPostId;

    @Column (name="commentedBy")
    private Integer commentedBy;

    @Column (name="commentText")
    private String commentText;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cmPostId", insertable = false, updatable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commentedBy", insertable = false, updatable = false)
    private Account account;

    public Comment(){}

    public Comment(Integer cmPostId, Integer commentedBy, String commentText){
        this.cmPostId = cmPostId;
        this.commentedBy = commentedBy;
        this.commentText = commentText;
    }

    public Comment(Integer commentId, Integer cmPostId, Integer commentedBy, String commentText){
        this.commentId = commentId;
        this.cmPostId = cmPostId;
        this.commentedBy = commentedBy;
        this.commentText = commentText;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public Integer getCmPostId() {
        return cmPostId;
    }

    public Integer getCommentedBy() {
        return commentedBy;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public void setCmPostId(Integer cmPostId) {
        this.cmPostId = cmPostId;
    }

    public void setCommentedBy(Integer commentedBy) {
        this.commentedBy = commentedBy;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getAccount(){
        return this.account.getUsername();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Comment other = (Comment) obj;
		if (cmPostId == null) {
			if (other.commentId != null)
				return false;
		} else if (!cmPostId.equals(other.commentId))
			return false;
		if (commentText == null) {
			if (other.commentText != null)
				return false;
		} else if (!commentText.equals(other.commentText))
			return false;
		if (commentedBy == null) {
			if (other.commentedBy != null)
				return false;
		} else if (!commentedBy.equals(other.commentedBy))
			return false;
		return true;
    }
}
