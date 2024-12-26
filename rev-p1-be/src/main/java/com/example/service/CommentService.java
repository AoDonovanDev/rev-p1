package com.example.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dto.CommentDto;
import com.example.entity.Account;
import com.example.entity.Comment;
import com.example.entity.Post;
import com.example.exception.AccountDoesNotExistException;
import com.example.exception.PostDoesNotExistException;
import com.example.repository.AccountRepository;
import com.example.repository.CommentRepository;
import com.example.repository.PostRepository;

@Service
@Transactional
public class CommentService {
    private CommentRepository commentRepository;
    private AccountRepository accountRepository;
    private PostRepository postRepository;


    @Autowired
    public CommentService(CommentRepository commentRepository, AccountRepository accountRepository, PostRepository postRepository){
        this.commentRepository = commentRepository;
        this.accountRepository = accountRepository;
        this.postRepository = postRepository;
    }

    public CommentDto createComment(Comment comment) throws AccountDoesNotExistException, PostDoesNotExistException{
        Optional<Account> account = accountRepository.findByAccountId(comment.getCommentedBy());
        Optional<Post> post = postRepository.findByPostId(comment.getCmPostId());
        if(account.get() == null){
            throw new AccountDoesNotExistException();
        } else if(post.get() == null){
            throw new PostDoesNotExistException();
        }
        Comment newComment = commentRepository.save(comment);
        CommentDto commentDto = new CommentDto(newComment.getCommentId(), newComment.getCmPostId(), newComment.getCommentedBy(), newComment.getCommentText());
        return commentDto;
    }
    
}
