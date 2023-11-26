package com.example.socialmedia.repositories;

import com.example.socialmedia.entities.Follower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowerRepository extends JpaRepository<Follower, Long> {
    List<Follower> findByFollowingAccountId(long accountId);
    Follower findByFollowingAccountIdAndFollowerAccountEmail(long accountId, String email);
    void deleteById(long id);
    List<Follower> findByFollowerAccountId(long accountId);
}
