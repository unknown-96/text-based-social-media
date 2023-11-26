package com.example.socialmedia.controllers;

import com.example.socialmedia.entities.Follower;
import com.example.socialmedia.objects.FollowerDTO;
import com.example.socialmedia.services.FollowerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FollowerController {
    private final FollowerService followerService;

    @PostMapping("/follow")
    public ResponseEntity<String> addFollower(@RequestBody @Valid FollowerDTO followerDTO) {
        Follower follower = followerService.addFollower(followerDTO);

        return ResponseEntity.ok(">> Added follower: " + follower.getFollowerAccount().getEmail());
    }

    @DeleteMapping("/unfollow")
    public ResponseEntity<String> removeFollower(@RequestBody @Valid FollowerDTO followerDTO) {
        followerService.removeFollower(followerDTO);

        return ResponseEntity.ok(">> Removed follower: " + followerDTO.getEmail());
    }

    @GetMapping("/followers/{accountId}")
    public ResponseEntity<List<String>> getFollowerAccounts(@PathVariable long accountId) {
        return new ResponseEntity<>(followerService.getFollowerAccountsEmails(accountId), HttpStatus.OK);
    }
}
