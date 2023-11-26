package com.example.socialmedia.controllers;

import com.example.socialmedia.exception.AccountExistsException;
import com.example.socialmedia.objects.AccountDTO;
import com.example.socialmedia.services.AccountService;
import com.example.socialmedia.entities.Account;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid AccountDTO accountDTO) throws AccountExistsException  {
        Account account = accountService.register(accountDTO);

        return ResponseEntity.ok(">> Registered account: " + account.getEmail());
    }
}