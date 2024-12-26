package com.example.service;

import java.lang.classfile.ClassFile.Option;
import java.util.List;
import java.util.Optional;

import org.apache.naming.java.javaURLContextFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.Post;
import com.example.entity.PostLike;
import com.example.exception.AccountDoesNotExistException;
import com.example.exception.AccountInfoException;
import com.example.exception.InvalidPostException;
import com.example.repository.AccountRepository;
import com.example.repository.PostLikeRepository;
import com.example.repository.PostRepository;

import io.jsonwebtoken.JwtException;

import com.example.dto.PostDto;
import com.example.entity.Account;

@Service
@Transactional
public class PostService {

    PostRepository postRepository;

    AccountRepository accountRepository;

    PostLikeRepository postLikeRepository;

    JwtService jwtService;

    @Autowired
    public PostService(PostRepository postRepository, AccountRepository accountRepository, PostLikeRepository postLikeRepository, JwtService jwtService) {
        this.postRepository = postRepository;
        this.accountRepository = accountRepository;
        this.postLikeRepository = postLikeRepository;
        this.jwtService = jwtService;
    }

    public PostDto createPost(Post post) throws InvalidPostException{
        Optional<Account> accOpt = accountRepository.findByAccountId(post.getPostedBy());
        Account account = accOpt.get();
        if(post.getPostText().length() > 255 || post.getPostText().length() == 0 || account==null) {
            throw new InvalidPostException();
        }
        System.err.println("*************************post obj in post service: " + post);
        Post newPost = postRepository.save(post);
        PostDto postDto = new PostDto(newPost.getPostId(), account.getUsername(), newPost.getPostText(), newPost.getTimePostedEpoch(), newPost.getPostedBy());
        return postDto;
        
    }

    public Optional<Post> deletePost(Integer postId){
        Optional<Post> postOpt = postRepository.findByPostId(postId);
        if(postOpt.isPresent()) {
            postRepository.delete(postOpt.get());
        }
        
        return postOpt;
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Optional<Post> getPostById(Integer postId) {
        return postRepository.findByPostId(postId);
    }

    public void updatePost(Integer post_id, String postText) throws InvalidPostException {
        Optional<Post> postOpt = postRepository.findByPostId(post_id);
        if(postOpt.isPresent() && postText.length() < 255 && postText.length() > 0){
            Post post = postOpt.get();
            post.setPostText(postText);
            postRepository.save(post);
            postOpt = Optional.of(post);
        } else {
            throw new InvalidPostException();
        }
    }

    public List<Post> getPostsByUser(Integer accountId) {
        return accountRepository.findById(accountId).get().getPosts();
    }

    public List<Post> getPostsByFollowing(String token) throws AccountDoesNotExistException, JwtException, AccountInfoException{
        Integer accountId = jwtService.returnAccountIdFromToken(token);
        Account account = accountRepository.findByAccountId(accountId).get();
        System.err.println("account in service: " + account);
        return account.getPostsByFollowing();
    }

    public void addLike(Integer accountId, Integer postId){
        PostLike pl = new PostLike(accountId, postId);
        postLikeRepository.save(pl);
    }

    public void removeLike(Integer accountId, Integer postId){
        PostLike pl = postLikeRepository.findByPlAccountIdAndPlPostId(accountId, postId).get();
        postLikeRepository.delete(pl);
    }

    public List<Post> searchPosts(String searchTerm){
        String withWildcard = "%" + searchTerm + "%";
        List<Post> lp = postRepository.findByPostTextLike(withWildcard);
        return lp;
    }
}
