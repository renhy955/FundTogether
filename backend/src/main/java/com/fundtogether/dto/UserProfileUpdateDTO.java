package com.fundtogether.dto;

import lombok.Data;

@Data
public class UserProfileUpdateDTO {
    private String nickname;
    private String avatar;
    private String bio;
    private String education;
    private String birthday; // Can be parsed as string and then to LocalDate
    private String company;
    private String profession;
    private String location;
    private Integer gender;
}
