package com.example.socialmedia.services;

import com.example.socialmedia.entities.Account;
import com.example.socialmedia.exception.AccountExistsException;
import com.example.socialmedia.objects.AccountDTO;
import com.example.socialmedia.repositories.AccountRepository;
import com.example.socialmedia.security.AccountDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {
    private final AccountRepository accountRepository;

    public Account register(AccountDTO accountDTO) throws AccountExistsException{
        Account account = accountRepository.findByEmail(accountDTO.getEmail());

        if (account != null) {
            throw new AccountExistsException();
        }

        return accountRepository.save(toAccount(accountDTO));
    }

    public Account getAccount(long id) {
        return accountRepository.findById(id);
    }
    public Account getAccountByEmail(String email) {
        return accountRepository.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return new AccountDetails(accountRepository.findByEmail(email));
    }

    private Account toAccount(AccountDTO accountDTO) {
        Account account = new Account();

        account.setPlan(accountDTO.getPlan());
        account.setEmail(accountDTO.getEmail());
        account.setPassword(new BCryptPasswordEncoder().encode(accountDTO.getPassword()));

        return account;
    }
}