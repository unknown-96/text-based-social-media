package com.example.socialmedia.controllers;

import com.example.socialmedia.entities.Account;
import com.example.socialmedia.entities.Post;
import com.example.socialmedia.exception.TooLongTextException;
import com.example.socialmedia.services.FollowerService;
import com.example.socialmedia.objects.PostDTO;
import com.example.socialmedia.services.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final FollowerService followerService;

    @PostMapping("/posts")
    public ResponseEntity<String> addPost(@RequestBody @Valid PostDTO postDTO) throws TooLongTextException {
        Post post = postService.addPost(postDTO);

        return ResponseEntity.ok(">> Added post: " + post.getId());
    }

    @GetMapping("/own-posts/{accountId}")
    public ResponseEntity<List<PostDTO>> getOwnPosts(@PathVariable long accountId) {
        List<PostDTO> postDTOList = postService.getPostDTOList(accountId);

        return new ResponseEntity<>(postDTOList, HttpStatus.OK);
    }

    @GetMapping("/relevant-posts/{accountId}")
    public ResponseEntity<List<PostDTO>> getRelevantPosts(@PathVariable long accountId) {
        List<Account> accountList = followerService.getFollowingAccounts(accountId);
        List<PostDTO> postDTOList = postService.getFollowingAccountsPosts(accountList);

        return new ResponseEntity<>(postDTOList, HttpStatus.OK);
    }
}