package com.example.socialmedia.objects;

public class FollowerTestDTO {
    public static FollowerDTO getDummyFollower(long accountId, String email) {
        FollowerDTO followerDTO = new FollowerDTO();

        followerDTO.setAccountId(accountId);
        followerDTO.setEmail(email);

        return followerDTO;
    }
}
