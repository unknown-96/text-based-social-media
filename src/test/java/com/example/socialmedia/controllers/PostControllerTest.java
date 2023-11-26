package com.example.socialmedia.controllers;

import com.example.socialmedia.entities.Post;
import com.example.socialmedia.objects.AccountDTO;
import com.example.socialmedia.objects.AccountTestDTO;
import com.example.socialmedia.objects.PostDTO;
import com.example.socialmedia.objects.PostTestDTO;
import com.example.socialmedia.repositories.AccountRepository;
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
public class PostControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired private WebApplicationContext webApplicationContext;
    @Autowired private AccountRepository accountRepository;
    @Autowired private PostRepository postRepository;
    private MockMvc mockMvc;

    @BeforeAll
    public void beforeAll() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }

    @AfterEach
    public void afterEach() {
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

    @Test
    public void notTooLongPostTest() throws Exception {
        AccountDTO accountDTO = AccountTestDTO.getFirstDummyAccount();

        performDummyRegistration(accountDTO);

        long accountId = accountRepository.findAll().get(0).getId();

        PostDTO postDTO = PostTestDTO.getDummyPost(accountId, 1000);

        mockMvc.perform(
                post("/posts")
                        .with(httpBasic(accountDTO.getEmail(), accountDTO.getPassword()))
                        .content(String.valueOf(objectMapper.writeValueAsString(postDTO)))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        Post post = postRepository.findAll().get(0);
        assertEquals(post.getText(), postDTO.getText());
    }

    @Test
    public void tooLongPostTest() throws Exception {
        AccountDTO accountDTO = AccountTestDTO.getFirstDummyAccount();

        performDummyRegistration(accountDTO);

        long accountId = accountRepository.findAll().get(0).getId();

        PostDTO postDTO = PostTestDTO.getDummyPost(accountId, 1001);

        mockMvc.perform(
                post("/posts")
                        .with(httpBasic(accountDTO.getEmail(), accountDTO.getPassword()))
                        .content(String.valueOf(objectMapper.writeValueAsString(postDTO)))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }
}
