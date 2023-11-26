package com.example.socialmedia.repositories;

import com.example.socialmedia.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findById(long id);
    Account findByEmail(String email);
}