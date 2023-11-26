package com.example.socialmedia.objects;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class CommentDTO {
    private long accountId;
    private String text;
    private String timestamp;
}
