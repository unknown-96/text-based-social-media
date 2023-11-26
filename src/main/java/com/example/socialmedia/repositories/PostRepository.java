package com.example.socialmedia.repositories;

import com.example.socialmedia.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Post findById(long id);
    List<Post> findByAccountIdOrderByTimestampDesc(long accountId);
}
