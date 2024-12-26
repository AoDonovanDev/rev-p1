package com.example.controller;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.RequestEntity.BodyBuilder;
import org.springframework.web.bind.annotation.*;

import com.example.dto.AccInfoDto;
import com.example.dto.CommentDto;
import com.example.dto.PostDto;
import com.example.entity.Account;
import com.example.entity.AuthDto;
import com.example.entity.Comment;
import com.example.entity.Follow;
import com.example.entity.Post;
import com.example.entity.PostLike;
import com.example.exception.AccountAlreadyExistsException;
import com.example.exception.AccountDoesNotExistException;
import com.example.exception.AccountInfoException;
import com.example.exception.InvalidPostException;
import com.example.exception.InvalidUsernamePasswordException;
import com.example.exception.PostDoesNotExistException;
import com.example.repository.AccountRepository;
import com.example.service.AccountService;
import com.example.service.CommentService;
import com.example.service.JwtService;
import com.example.service.PostService;

import io.jsonwebtoken.JwtException;


@RestController
public class SocialMediaController {

    @Autowired
    AccountService accountService;

    @Autowired
    PostService postService;

    @Autowired
    CommentService commentService;

    @Autowired
    JwtService jwtService;


    @PostMapping("/register")
    public @ResponseBody ResponseEntity<AuthDto> createAccount(@RequestBody Account account) {
        Account newAccount;
        try {
            newAccount = accountService.createAccount(account);
            String token = jwtService.generateAccountToken(newAccount);
            AuthDto authDto = new AuthDto(true, token);
            return ResponseEntity.status(200).body(authDto); 
        } catch(InvalidUsernamePasswordException | AccountAlreadyExistsException e){
            e.printStackTrace();
            if(e.getClass().getName().contains("AccountAlreadyExistsException")){
                return ResponseEntity.status(409).body(null);
            } else {
                return ResponseEntity.status(400).body(null);
            }
        }
    
    }

    @PostMapping("/login")
    public @ResponseBody ResponseEntity<AuthDto> login(@RequestBody Account account) {
        try {
            AuthDto authDto = accountService.login(account);
            return ResponseEntity.status(200).body(authDto);
        } catch (AccountDoesNotExistException | InvalidUsernamePasswordException e) {
            e.printStackTrace();
            return ResponseEntity.status(401).body(new AuthDto(false, null));
        }
    }

    @PostMapping("/token")
    public @ResponseBody ResponseEntity<AuthDto> validateAndRefreshToken(@RequestBody String token){
        try {
            AuthDto authDto = accountService.validateAndRefresh(token);
            return ResponseEntity.status(200).body(authDto);
        } catch(Exception e){
            return ResponseEntity.status(500).body(new AuthDto(false, null));
        }
    }

    @PostMapping("/posts") 
    public @ResponseBody ResponseEntity<PostDto> createPost(@RequestBody Post post) {
        try {
            PostDto newPost = postService.createPost(post);
            return ResponseEntity.status(200).body(newPost);
        } catch (InvalidPostException e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body(null);
        }

        
    }
    @GetMapping("/posts") 
    public @ResponseBody ResponseEntity<List<Post>> getAllPosts() {
        List<Post> msgs = postService.getAllPosts();
        return ResponseEntity.status(200).body(msgs);
    }
    
    @GetMapping("/posts/{id}")
    public @ResponseBody ResponseEntity<Post> getPostByID(@PathVariable Integer id) {
        Optional<Post> postOpt = postService.getPostById(id);
        if(postOpt.isPresent()) {
            return ResponseEntity.status(200).body(postOpt.get());
        }
        return ResponseEntity.status(200).body(null);
    }

    @PostMapping("/search/posts")
    public @ResponseBody ResponseEntity<List<Post>> searchPosts(@RequestBody String searchTerm){
        List<Post> lp = postService.searchPosts(searchTerm);
        return ResponseEntity.status(200).body(lp);
    }


    @PatchMapping("/posts/{id}")
    public @ResponseBody ResponseEntity<Integer> updatePost(@PathVariable Integer id, @RequestBody Post post) {
        try {
            postService.updatePost(id, post.getPostText());
            return ResponseEntity.status(200).body(1);
        } catch (InvalidPostException e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body(null);
        }
    }

    @GetMapping("/accounts/{account_id}/posts")
    public @ResponseBody ResponseEntity<List<Post>> getPostsByUser(@PathVariable Integer account_id) {
        List<Post> msgs = postService.getPostsByUser(account_id);
        return ResponseEntity.status(200).body(msgs);
    }

    @GetMapping("/accounts/{account_id}")
    public @ResponseBody ResponseEntity<AccInfoDto> getAccountByAccountId(@PathVariable Integer account_id){
        try{
            AccInfoDto accInfoDto = accountService.getAccountByAccountId(account_id);
            return ResponseEntity.status(200).body(accInfoDto);
        } catch(AccountDoesNotExistException e){
            e.printStackTrace();
            return ResponseEntity.status(500).body(new AccInfoDto(false, null));   
        }
    }


    @PostMapping("/accountInfo")
    public @ResponseBody ResponseEntity<AccInfoDto> getUserInfo(@RequestBody String token){
        try {
            Account account = accountService.getAccountInfo(token);
            return ResponseEntity.status(200).body(new AccInfoDto(true, account));
        } catch(Exception e){
            return ResponseEntity.status(500).body(new AccInfoDto(false, null));
        }
    }

    //protecc and must be same
    @PatchMapping("/account/{account_id}/edit")
    public @ResponseBody ResponseEntity<AccInfoDto> editUserInfo(
        @RequestHeader("Authorization") String token, 
        @PathVariable Integer account_id, 
        @RequestBody Account accountWithChanges){
        try {
            Integer accId = jwtService.returnAccountIdFromToken(token);
            if(accId != account_id){
                return ResponseEntity.status(400).body(new AccInfoDto(false, null));
            }
        } catch(JwtException | AccountInfoException e){
            e.printStackTrace();
            return ResponseEntity.status(500).body(new AccInfoDto(false, null));
        }
        try {
            AccInfoDto accInfo = accountService.editAccountInfo(account_id, accountWithChanges);
            System.err.println(accInfo);
            return ResponseEntity.status(200).body(accInfo);
        } catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.status(500).body(new AccInfoDto(false, null));
        }
    }

    @PostMapping("/following/posts")
    public @ResponseBody ResponseEntity<List<Post>> getPostsByFollowing(@RequestBody String token){
        System.err.println("token in controller: " + token);
        try {
            List<Post> postsByFollowing = postService.getPostsByFollowing(token);
            System.err.println("posts in controller: " + postsByFollowing);
            return ResponseEntity.status(200).body(postsByFollowing);
        } catch(AccountDoesNotExistException | AccountInfoException | JwtException e){
            e.printStackTrace();
            return ResponseEntity.status(400).body(null);
        }
         
     }
 
     //protecc
    @DeleteMapping("/posts/{id}")
    public @ResponseBody ResponseEntity<Integer> deletePost(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        try {
            Integer accId = jwtService.returnAccountIdFromToken(token);
            Integer postedById = postService.getPostById(id).get().getPostedBy();
            if(accId != postedById){
                return ResponseEntity.status(400).body(null);
            }
        } catch(JwtException | AccountInfoException e){
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
        Optional<Post> postOpt = postService.deletePost(id);
        if(postOpt.isPresent()) {
            return ResponseEntity.status(200).body(1);
        }
        return ResponseEntity.status(200).body(null);
    }

    //protecc
    @PostMapping("/posts/addLike")
    public @ResponseBody ResponseEntity<Object> addLike(@RequestBody PostLike postLike){
        postService.addLike(postLike.getPlAccountId(), postLike.getPlPostId());
        return ResponseEntity.status(204).body(null);
    }

    //protecc
    @PostMapping("/posts/removeLike")
    public @ResponseBody ResponseEntity<Object> removeLike(@RequestBody PostLike postLike){
        postService.removeLike(postLike.getPlAccountId(), postLike.getPlPostId());
        return ResponseEntity.status(204).body(null);
    }

    //protecc
    @PostMapping("/comments")
    public @ResponseBody ResponseEntity<CommentDto> addComment(@RequestBody Comment comment){
        try {
            CommentDto commentDto = commentService.createComment(comment);
            return ResponseEntity.status(200).body(commentDto);
        } catch(AccountDoesNotExistException | PostDoesNotExistException e){
            e.printStackTrace();
            return ResponseEntity.status(400).body(null);
        }
    }

    //protecc
    @PostMapping("/follow/add")
    public @ResponseBody ResponseEntity<Object> addFollow(@RequestBody Follow follow){
        try {
            accountService.addFollow(follow);
            return ResponseEntity.status(200).body(null);
        } catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.status(400).body(null);
        }
    }

    //protecc
    @PostMapping("/follow/remove")
    public @ResponseBody ResponseEntity<Object> removeFollow(@RequestBody Follow follow){
        try {
            accountService.removeFollow(follow);
            return ResponseEntity.status(200).body(null);
        } catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.status(400).body(null);
        }
    }
}
