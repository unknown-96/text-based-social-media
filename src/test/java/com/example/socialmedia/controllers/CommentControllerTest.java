package com.example.socialmedia.controllers;

import com.example.socialmedia.entities.Comment;
import com.example.socialmedia.objects.*;
import com.example.socialmedia.repositories.AccountRepository;
import com.example.socialmedia.repositories.CommentRepository;
import com.example.socialmedia.repositories.PostRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CommentControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired private WebApplicationContext webApplicationContext;
    @Autowired private AccountRepository accountRepository;
    @Autowired private PostRepository postRepository;
    @Autowired private CommentRepository commentRepository;
    private MockMvc mockMvc;

    @BeforeAll
    public void beforeAll() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }

    @AfterEach
    public void afterEach() {
        commentRepository.deleteAll();
        commentRepository.flush();

        postRepository.deleteAll();
        postRepository.flush();

        accountRepository.deleteAll();
        accountRepository.flush();
    }

    public void performDummyRegistration(AccountDTO accountDTO) throws Exception {
        mockMvc.perform(
                post("/register")
                        .content(String.valueOf(objectMapper.writeValueAsString(accountDTO)))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    public void performDummyPost(AccountDTO accountDTO, PostDTO postDTO) throws Exception {
        mockMvc.perform(
                post("/posts")
                        .with(httpBasic(accountDTO.getEmail(), accountDTO.getPassword()))
                        .content(String.valueOf(objectMapper.writeValueAsString(postDTO)))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    public void notTooLongCommentTest() throws Exception {
        AccountDTO accountDTO = AccountTestDTO.getFirstDummyAccount();
        performDummyRegistration(accountDTO);

        long accountId = accountRepository.findAll().get(0).getId();

        PostDTO postDTO = PostTestDTO.getDummyPost(accountId, 1000);
        performDummyPost(accountDTO, postDTO);

        long postId = postRepository.findAll().get(0).getId();
        CommentDTO commentDTO = CommentTestDTO.getDummyComment(accountId, 1000);

        mockMvc.perform(
                post("/posts/" + postId + "/comments")
                        .with(httpBasic(accountDTO.getEmail(), accountDTO.getPassword()))
                        .content(String.valueOf(objectMapper.writeValueAsString(commentDTO)))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        Comment comment = commentRepository.findAll().get(0);
        assertEquals(comment.getText(), commentDTO.getText());
    }

    @Test
    public void tooLongCommentTest() throws Exception {
        AccountDTO accountDTO = AccountTestDTO.getFirstDummyAccount();
        performDummyRegistration(accountDTO);

        long accountId = accountRepository.findAll().get(0).getId();

        PostDTO postDTO = PostTestDTO.getDummyPost(accountId, 1000);
        performDummyPost(accountDTO, postDTO);

        long postId = postRepository.findAll().get(0).getId();
        CommentDTO commentDTO = CommentTestDTO.getDummyComment(accountId, 1001);

        mockMvc.perform(
                post("/posts/" + postId + "/comments")
                        .with(httpBasic(accountDTO.getEmail(), accountDTO.getPassword()))
                        .content(String.valueOf(objectMapper.writeValueAsString(commentDTO)))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void notTooManyCommentsTest() throws Exception {
        AccountDTO accountDTO = AccountTestDTO.getFirstDummyAccount();
        performDummyRegistration(accountDTO);

        long accountId = accountRepository.findAll().get(0).getId();

        PostDTO postDTO = PostTestDTO.getDummyPost(accountId,1000);
        performDummyPost(accountDTO, postDTO);

        long postId = postRepository.findAll().get(0).getId();
        CommentDTO commentDTO = CommentTestDTO.getDummyComment(accountId, 1000);

        for (int i = 0; i < 10; i++) {
            mockMvc.perform(
                    post("/posts/" + postId + "/comments")
                            .with(httpBasic(accountDTO.getEmail(), accountDTO.getPassword()))
                            .content(String.valueOf(objectMapper.writeValueAsString(commentDTO)))
                            .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk());
        }
    }

    @Test
    public void tooManyCommentsTest() throws Exception {
        AccountDTO accountDTO = AccountTestDTO.getFirstDummyAccount();
        performDummyRegistration(accountDTO);

        long accountId = accountRepository.findAll().get(0).getId();

        PostDTO postDTO = PostTestDTO.getDummyPost(accountId,1000);
        performDummyPost(accountDTO, postDTO);

        long postId = postRepository.findAll().get(0).getId();
        CommentDTO commentDTO = CommentTestDTO.getDummyComment(accountId, 1000);

        for (int i = 0; i < 10; i++) {
            mockMvc.perform(
                    post("/posts/" + postId + "/comments")
                            .with(httpBasic(accountDTO.getEmail(), accountDTO.getPassword()))
                            .content(String.valueOf(objectMapper.writeValueAsString(commentDTO)))
                            .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk());
        }

        mockMvc.perform(
                post("/posts/" + postId + "/comments")
                        .with(httpBasic(accountDTO.getEmail(), accountDTO.getPassword()))
                        .content(String.valueOf(objectMapper.writeValueAsString(commentDTO)))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }
}
