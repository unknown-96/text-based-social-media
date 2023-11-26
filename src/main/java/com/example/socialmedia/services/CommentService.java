package com.example.socialmedia.services;

import com.example.socialmedia.entities.Comment;
import com.example.socialmedia.exception.TooLongTextException;
import com.example.socialmedia.exception.TooManyCommentsException;
import com.example.socialmedia.objects.CommentDTO;
import com.example.socialmedia.entities.Post;
import com.example.socialmedia.repositories.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostService postService;
    private final AccountService accountService;

    public Comment addComment(long postId, CommentDTO commentDTO) throws TooManyCommentsException, TooLongTextException {
        List<Comment> commentList = commentRepository.findByPostIdAndAccountId(postId, commentDTO.getAccountId());

        if (commentList.size() == 10) {
            throw new TooManyCommentsException();
        }

        if (commentDTO.getText().length() > 1000) {
            throw new TooLongTextException();
        }

        Comment comment = toComment(postId, commentDTO);

        return commentRepository.save(comment);
    }

    public List<CommentDTO> getLatestComments(long postId) {
        List<Comment> commentList = commentRepository.findTop100ByPostIdOrderByTimestampDesc(postId);
        List<CommentDTO> commentDTOList = new ArrayList<>();

        for (Comment comment: commentList) {
            commentDTOList.add(toDTO(comment));
        }

        return commentDTOList;
    }

    public List<CommentDTO> getRelevantComments(List<Post> postList) {
        List<CommentDTO> commentDTOList = new ArrayList<>();

        for (Post post: postList) {
            List<Comment> commentList = commentRepository.findByPostIdOrderByTimestamp(post.getId());

            for (Comment comment: commentList) {
                commentDTOList.add(toDTO(comment));
            }
        }

        return commentDTOList;
    }

    private Comment toComment(long postId, CommentDTO commentDTO) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        Comment comment = new Comment();

        comment.setPost(postService.getPost(postId));
        comment.setAccount(accountService.getAccount(commentDTO.getAccountId()));
        comment.setText(commentDTO.getText());
        comment.setTimestamp(LocalDateTime.parse(commentDTO.getTimestamp(), dateTimeFormatter));

        return comment;
    }

    private CommentDTO toDTO(Comment comment) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        CommentDTO commentDTO = new CommentDTO();

        commentDTO.setAccountId(comment.getAccount().getId());
        commentDTO.setText(comment.getText());
        commentDTO.setTimestamp(comment.getTimestamp().format(dateTimeFormatter));

        return commentDTO;
    }
}