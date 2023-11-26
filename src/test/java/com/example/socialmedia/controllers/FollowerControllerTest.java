package com.example.socialmedia.controllers;

import com.example.socialmedia.entities.Account;
import com.example.socialmedia.entities.Follower;
import com.example.socialmedia.objects.AccountDTO;
import com.example.socialmedia.objects.AccountTestDTO;
import com.example.socialmedia.objects.FollowerDTO;
import com.example.socialmedia.objects.FollowerTestDTO;
import com.example.socialmedia.repositories.AccountRepository;
import com.example.socialmedia.repositories.FollowerRepository;
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
public class FollowerControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired private WebApplicationContext webApplicationContext;
    @Autowired private AccountRepository accountRepository;
    @Autowired private FollowerRepository followerRepository;
    private MockMvc mockMvc;

    @BeforeAll
    public void beforeAll() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }

    @AfterEach
    public void afterEach() {
        followerRepository.deleteAll();
        followerRepository.flush();

        accountRepository.deleteAll();
        accountRepository.flush();
    }

    public void performDummyRegistration(AccountDTO accountDTO, AccountDTO accountDTO2) throws Exception {
        mockMvc.perform(
                post("/register")
                        .content(String.valueOf(objectMapper.writeValueAsString(accountDTO)))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        mockMvc.perform(
                post("/register")
                        .content(String.valueOf(objectMapper.writeValueAsString(accountDTO2)))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    public void followerValidationTest() throws Exception {
        AccountDTO accountDTO = AccountTestDTO.getFirstDummyAccount();
        AccountDTO accountDTO2 = AccountTestDTO.getSecondDummyAccount();

        performDummyRegistration(accountDTO, accountDTO2);

        long accountId = accountRepository.findAll().get(0).getId();

        long accountId2 = accountRepository.findAll().get(1).getId();
        Account account2 = accountRepository.findById(accountId2);

        FollowerDTO followerDTO = FollowerTestDTO.getDummyFollower(accountId, account2.getEmail());

        mockMvc.perform(
                post("/follow")
                        .with(httpBasic(accountDTO2.getEmail(), accountDTO2.getPassword()))
                        .content(String.valueOf(objectMapper.writeValueAsString(followerDTO)))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        Follower follower = followerRepository.findAll().get(0);

        assertEquals(follower.getFollowingAccount().getId(), accountId);
        assertEquals(follower.getFollowerAccount().getEmail(), account2.getEmail());
    }
}