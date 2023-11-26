package com.example.socialmedia.objects;

import java.util.Arrays;

public class CommentTestDTO {
    public static CommentDTO getDummyComment(long accountId, int strLen) {
        char[] chars = new char[strLen];
        Arrays.fill(chars, 'a');

        CommentDTO commentDTO = new CommentDTO();

        commentDTO.setAccountId(accountId);
        commentDTO.setText(String.valueOf(chars));
        commentDTO.setTimestamp("2023-10-21 06:00");

        return commentDTO;
    }
}
