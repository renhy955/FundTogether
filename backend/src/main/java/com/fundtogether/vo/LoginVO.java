package com.fundtogether.vo;

import lombok.Data;

@Data
public class LoginVO {
    private String token;
    private Long id;
    private String account;
    private String nickname;
    private String avatar;
    private Integer role;
}
