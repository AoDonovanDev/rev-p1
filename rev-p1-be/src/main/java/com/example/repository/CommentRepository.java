package com.example.repository;
import java.util.List;
import com.example.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommentRepository extends JpaRepository<Comment, Integer>{
    public List<Comment> findByCommentedBy(Integer commentedBy);
}
