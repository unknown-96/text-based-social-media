package com.example.socialmedia.objects;

import java.util.Arrays;

public class PostTestDTO {
    public static PostDTO getDummyPost(long accountId, int strLen) {
        char[] chars = new char[strLen];
        Arrays.fill(chars, 'a');

        PostDTO postDTO = new PostDTO();

        postDTO.setAccountId(accountId);
        postDTO.setText(String.valueOf(chars));
        postDTO.setTimestamp("2023-10-21 00:00");

        return postDTO;
    }
}
