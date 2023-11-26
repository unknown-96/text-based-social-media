package com.example.socialmedia.objects;

public class AccountTestDTO {
    public static AccountDTO getFirstDummyAccount() {
        AccountDTO accountDTO = new AccountDTO();

        accountDTO.setPlan("Free");
        accountDTO.setEmail("first@account.com");
        accountDTO.setPassword("first-password");

        return accountDTO;
    }

    public static AccountDTO getSecondDummyAccount() {
        AccountDTO accountDTO = new AccountDTO();

        accountDTO.setPlan("Free");
        accountDTO.setEmail("second@account.com");
        accountDTO.setPassword("second-password");

        return accountDTO;
    }
}
