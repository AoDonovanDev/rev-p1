package com.example.entity;

import javax.persistence.*;

@Entity
@Table(name="follow")
public class Follow {
    
    @Column (name="followId")
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer followId;

    @Column (name="followingAccountId")
    private Integer followingAccountId;

    @Column (name="followedAccountId")
    private Integer followedAccountId;


    public Follow(){}

    public Follow(Integer followingAccountId, Integer followedAccountId){
        this.followedAccountId = followingAccountId;
        this.followedAccountId = followedAccountId;
    }

    public Integer getFollowingAccountId() {
        return followingAccountId;
    }

    public void setFollowingAccountId(Integer followingAccountId) {
        this.followingAccountId = followingAccountId;
    }

    public Integer getFollowedAccountId() {
        return followedAccountId;
    }

    public void setFollowedAccountId(Integer followedAccountId) {
        this.followedAccountId = followedAccountId;
    }
   
    @Override
    public boolean equals(Object obj) {
        if(this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
        Follow other = (Follow) obj;
        if(this.followedAccountId != other.followedAccountId)
            return false;
        if(this.followingAccountId != other.followingAccountId)
            return false;
        return true;
    }
}
