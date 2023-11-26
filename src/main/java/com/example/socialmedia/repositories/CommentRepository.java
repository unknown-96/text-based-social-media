package com.example.socialmedia.repositories;

import com.example.socialmedia.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostIdAndAccountId(long postId, long accountId);
    List<Comment> findTop100ByPostIdOrderByTimestampDesc(long postId);
    List<Comment> findByPostIdOrderByTimestamp(long postId);
}
