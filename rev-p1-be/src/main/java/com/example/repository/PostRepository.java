package com.example.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Post;

public interface PostRepository extends JpaRepository<Post, Integer>{

    public Optional<Post> findByPostId(Integer postId);

    public List<Post> findByPostedBy(Integer postedBy);

    public List<Post> findByPostTextLike(String postText);
}
