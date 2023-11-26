package com.example.socialmedia.controllers;

import com.example.socialmedia.exception.TooLongTextException;
import com.example.socialmedia.exception.TooManyCommentsException;
import com.example.socialmedia.objects.CommentDTO;
import com.example.socialmedia.services.CommentService;
import com.example.socialmedia.entities.Account;
import com.example.socialmedia.entities.Comment;
import com.example.socialmedia.services.AccountService;
import com.example.socialmedia.services.FollowerService;
import com.example.socialmedia.entities.Post;
import com.example.socialmedia.services.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final FollowerService followerService;
    private final AccountService accountService;
    private final PostService postService;

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<String> addComment(@PathVariable long postId, @RequestBody @Valid CommentDTO commentDTO) throws TooManyCommentsException, TooLongTextException {
        Comment comment = commentService.addComment(postId, commentDTO);

        return ResponseEntity.ok(">> Added comment: " + comment.getId());
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommentDTO>> getLatestComments(@PathVariable long postId) {
        List<CommentDTO> commentDTOList = commentService.getLatestComments(postId);

        return new ResponseEntity(commentDTOList, HttpStatus.OK);
    }


    @GetMapping("/relevant-comments/{accountId}")
    public ResponseEntity<List<CommentDTO>> getRelevantComments(@PathVariable long accountId) {
        List<Account> accountList = followerService.getFollowingAccounts(accountId);
        Account ownAccount = accountService.getAccount(accountId);

        accountList.add(ownAccount);

        List<Post> postList = postService.getRelevantPosts(accountList);
        List<CommentDTO> commentDTOList = commentService.getRelevantComments(postList);

        return new ResponseEntity<>(commentDTOList, HttpStatus.OK);
    }
}