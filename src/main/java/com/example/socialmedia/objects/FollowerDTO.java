package com.example.socialmedia.objects;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class FollowerDTO {
    private long accountId;
    private String email;
}
