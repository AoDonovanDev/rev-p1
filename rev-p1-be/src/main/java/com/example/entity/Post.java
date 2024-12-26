package com.example.entity;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="post")
public class Post {
   
     @Column
     @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer postId;
    
    @Column (name="postedBy")
    private Integer postedBy;
  
    @Column (name="postText")
    private String postText;
 
    @Column (name="timePostedEpoch")
    private Long timePostedEpoch;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="postedBy", insertable = false, updatable = false)
    private Account account;
    
    @OneToMany(mappedBy = "cmPostId", cascade = CascadeType.REMOVE)
    private List<Comment> comments;

    @OneToMany(mappedBy = "plPostId")
    private List<PostLike> postLikes;

   
    public Post(){
    }
 
    public Post(Integer postedBy, String postText, Long timePostedEpoch) {
        this.postedBy = postedBy;
        this.postText = postText;
        this.timePostedEpoch = timePostedEpoch;
    }
   
    public Post(Integer postId, Integer postedBy, String postText, Long timePostedEpoch) {
        this.postId = postId;
        this.postedBy = postedBy;
        this.postText = postText;
        this.timePostedEpoch = timePostedEpoch;
    }

    public Integer getPostId() {
        return postId;
    }
  
    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Integer getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(Integer postedBy) {
        this.postedBy = postedBy;
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
        String username = account.getUsername();
        return username;
    }

    public List<Comment> getComments(){
        return this.comments;
    }

    public List<Integer> getPostLikes() {
        List<Integer> pls = postLikes == null ? new ArrayList<>() : postLikes.stream().map(el -> el.getPlAccountId()).toList();
        return pls;
    }
  
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Post other = (Post) obj;
		if (postId == null) {
			if (other.postId != null)
				return false;
		} else if (!postId.equals(other.postId))
			return false;
		if (postText == null) {
			if (other.postText != null)
				return false;
		} else if (!postText.equals(other.postText))
			return false;
		if (postedBy == null) {
			if (other.postedBy != null)
				return false;
		} else if (!postedBy.equals(other.postedBy))
			return false;
		if (timePostedEpoch == null) {
			if (other.timePostedEpoch != null)
				return false;
		} else if (!timePostedEpoch.equals(other.timePostedEpoch))
			return false;
		return true;
	}
	
    @Override
    public String toString() {
        return "Post{" +
                "postId=" + postId +
                ", postedBy=" + postedBy +
                ", postText='" + postText + '\'' +
                ", timePostedEpoch=" + timePostedEpoch +
                '}';
    }


}
