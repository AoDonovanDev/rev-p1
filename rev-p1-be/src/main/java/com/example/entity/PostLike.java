package com.example.entity;

import javax.persistence.*;

@Entity
@Table(name="postlike")
public class PostLike {
    
    @Column (name="likeId")
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer likeId;

    @Column (name="plAccountId")
    private Integer plAccountId;

    @Column (name="plPostId")
    private Integer plPostId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plPostId", insertable = false, updatable = false)
    private Post post;

    public PostLike(){}
    
    public PostLike(Integer plAccountId, Integer plPostId){
        this.plAccountId = plAccountId;
        this.plPostId = plPostId;
    }

    public Integer getPlAccountId() {
        return plAccountId;
    }

    public void setPlAccountId(Integer plAccountId) {
        this.plAccountId = plAccountId;
    }

    public Integer getPlPostId() {
        return plPostId;
    }

    public void setPlPostId(Integer postId) {
        this.plPostId = postId;
    }

    public Integer getLikeId() {
        return likeId;
    }

    public void setLikeId(Integer likeId) {
        this.likeId = likeId;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
        PostLike other = (PostLike) obj;
        if(this.plAccountId != other.plAccountId)
            return false;
        if(this.plPostId != other.plPostId)
            return false;
        return true;
    }
}
