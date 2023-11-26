package com.example.socialmedia.controllers;

import com.example.socialmedia.entities.Account;
import com.example.socialmedia.objects.AccountDTO;
import com.example.socialmedia.objects.AccountTestDTO;
import com.example.socialmedia.repositories.AccountRepository;
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
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AccountControllerTest {
    @Autowired private ObjectMapper objectMapper;
    @Autowired private WebApplicationContext webApplicationContext;
    @Autowired private AccountRepository accountRepository;
    private MockMvc mockMvc;

    @BeforeAll
    public void beforeAll() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }

    @AfterEach
    public void afterEach() {
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
    public void registerTest() throws Exception {
        AccountDTO accountDTO = AccountTestDTO.getFirstDummyAccount();

        performDummyRegistration(accountDTO);

        Account account = accountRepository.findAll().get(0);

        assertEquals(account.getEmail(), accountDTO.getEmail());
    }

    @Test
    public void reRegisterTest() throws Exception {
        AccountDTO accountDTO = AccountTestDTO.getFirstDummyAccount();

        performDummyRegistration(accountDTO);

        mockMvc.perform(
                post("/register")
                        .content(String.valueOf(objectMapper.writeValueAsString(accountDTO)))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());
    }
}
