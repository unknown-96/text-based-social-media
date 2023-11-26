package com.example.socialmedia.services;

import com.example.socialmedia.entities.Account;
import com.example.socialmedia.entities.Follower;
import com.example.socialmedia.objects.FollowerDTO;
import com.example.socialmedia.repositories.FollowerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class FollowerService {
    private final FollowerRepository followerRepository;
    private final AccountService accountService;

    public Follower addFollower(FollowerDTO followerDTO) {
        return followerRepository.save(toFollower(followerDTO));
    }

    public void removeFollower(FollowerDTO followerDTO) {
        Follower follower =
                followerRepository.findByFollowingAccountIdAndFollowerAccountEmail(followerDTO.getAccountId(), followerDTO.getEmail());

        followerRepository.deleteById(follower.getId());
    }

    public List<Account> getFollowingAccounts(long accountId) {
        List<Follower> followerList = followerRepository.findByFollowerAccountId(accountId);
        List<Account> accountList = new ArrayList<>();

        for (Follower follower: followerList) {
            accountList.add(follower.getFollowingAccount());
        }

        return accountList;
    }

    public List<String> getFollowerAccountsEmails(long accountId) {
        List<Follower> followerList = followerRepository.findByFollowingAccountId(accountId);
        List<String> followerListEmailList = new ArrayList<>();

        for (Follower follower: followerList) {
            followerListEmailList.add(follower.getFollowerAccount().getEmail());
        }

        return followerListEmailList;
    }

    private Follower toFollower(FollowerDTO followerDTO) {
        Follower follower = new Follower();

        follower.setFollowingAccount(accountService.getAccount(followerDTO.getAccountId()));
        follower.setFollowerAccount(accountService.getAccountByEmail(followerDTO.getEmail()));

        return follower;
    }
}
