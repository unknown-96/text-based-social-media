package com.example.socialmedia.objects;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class AccountDTO {
    private String plan;
    private String email;
    private String password;
}