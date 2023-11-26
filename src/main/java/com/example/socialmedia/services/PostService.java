package com.example.socialmedia.services;

import com.example.socialmedia.entities.Account;
import com.example.socialmedia.entities.Post;
import com.example.socialmedia.exception.TooLongTextException;
import com.example.socialmedia.objects.PostDTO;
import com.example.socialmedia.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final AccountService accountService;

    public Post addPost(PostDTO postDTO) throws TooLongTextException {
        if (postDTO.getText().length() > 1000) {
            throw new TooLongTextException();
        }

        return postRepository.save(toPost(postDTO));
    }

    public Post getPost(long postId) {
        return postRepository.findById(postId);
    }

    public List<Post> getPostList(long accountId) {
        List<Post> postList = postRepository.findByAccountIdOrderByTimestampDesc(accountId);

        return postList;
    }

    public List<PostDTO> getPostDTOList(long accountId) {
        List<Post> postList = getPostList(accountId);
        List<PostDTO> postDTOList = new ArrayList<>();

        for (Post post: postList) {
            postDTOList.add(toDTO(post));
        }

        return postDTOList;
    }

    public List<PostDTO> getFollowingAccountsPosts(List<Account> followingAccountList) {
        List<PostDTO> postDTOList = new ArrayList<>();

        for (Account followingAccount: followingAccountList) {
            List<PostDTO> followingAccountPostDTOList = getPostDTOList(followingAccount.getId());

            for (PostDTO followingAccountPostDTO: followingAccountPostDTOList) {
                postDTOList.add(followingAccountPostDTO);
            }
        }

        return postDTOList;
    }

    public List<Post> getRelevantPosts(List<Account> accountList) {
        List<Post> postList = new ArrayList<>();

        for (Account account: accountList) {
            List<Post> accountPostList = getPostList(account.getId());

            for (Post post: accountPostList) {
                postList.add(post);
            }
        }

        return postList;
    }

    private Post toPost(PostDTO postDTO) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        Post post = new Post();

        post.setAccount(accountService.getAccount(postDTO.getAccountId()));
        post.setText(postDTO.getText());
        post.setTimestamp(LocalDateTime.parse(postDTO.getTimestamp(), dateTimeFormatter));

        return post;
    }

    private PostDTO toDTO(Post post) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        PostDTO postDTO = new PostDTO();

        postDTO.setAccountId(post.getAccount().getId());
        postDTO.setText(post.getText());
        postDTO.setTimestamp(post.getTimestamp().format(dateTimeFormatter));

        return postDTO;
    }
}